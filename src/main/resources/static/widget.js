(function () {
    "use strict";

    const script =
        document.currentScript ||
        document.querySelector("script[data-site-key]");

    if (!script) {
        console.error("Chatbot: widget script not found.");
        return;
    }

    const siteKey = script.dataset.siteKey;

    if (!siteKey) {
        console.error(
            "Chatbot: data-site-key is required."
        );
        return;
    }

    /*
     * By default, use the same origin that served widget.js.
     *
     * Production:
     * https://chatbot.example.com/widget.js
     *         ↓
     * https://chatbot.example.com/api/chat
     *
     * Development can override this:
     * data-api-url="http://localhost:8080"
     */
    const apiBaseUrl =
        script.dataset.apiUrl ||
        new URL(script.src).origin;

    /*
     * Each website gets its own browser-side conversation.
     *
     * Example:
     *
     * chatbot_conversation_pk_abc
     * chatbot_conversation_pk_xyz
     */
    const storageKey =
        "chatbot_conversation_" + siteKey;

    let conversationId =
        localStorage.getItem(storageKey);

    if (!conversationId) {
        conversationId = crypto.randomUUID();

        localStorage.setItem(
            storageKey,
            conversationId
        );
    }

    let isSending = false;

    /* ==============================
       STYLES
       ============================== */

    const style =
        document.createElement("style");

    style.textContent = `
        #chatbot-button {
            position: fixed;
            right: 24px;
            bottom: 24px;
            width: 60px;
            height: 60px;
            border: none;
            border-radius: 50%;
            background: #111827;
            color: white;
            font-size: 24px;
            cursor: pointer;
            z-index: 999999;
            box-shadow: 0 6px 24px rgba(0,0,0,0.25);
            transition:
                transform 0.2s ease,
                box-shadow 0.2s ease;
        }

        #chatbot-button:hover {
            transform: scale(1.06);
            box-shadow:
                0 8px 28px rgba(0,0,0,0.3);
        }

        #chatbot-window {
            position: fixed;
            right: 24px;
            bottom: 96px;

            width: 360px;
            height: 520px;

            background: white;

            border: 1px solid #e5e7eb;
            border-radius: 16px;

            box-shadow:
                0 15px 50px rgba(0,0,0,0.2);

            display: none;
            flex-direction: column;

            overflow: hidden;

            z-index: 999998;

            font-family:
                Arial,
                Helvetica,
                sans-serif;
        }

        #chatbot-header {
            min-height: 58px;

            display: flex;
            align-items: center;

            padding:
                0 16px;

            background: #111827;
            color: white;

            font-size: 16px;
            font-weight: 600;
        }

        #chatbot-messages {
            flex: 1;

            padding: 16px;

            overflow-y: auto;

            display: flex;
            flex-direction: column;

            gap: 10px;

            background: #ffffff;
        }

        .chatbot-message {
            max-width: 82%;

            padding:
                10px 14px;

            border-radius: 14px;

            line-height: 1.45;

            font-size: 14px;

            white-space: pre-wrap;

            word-break: break-word;
        }

        .chatbot-user {
            align-self: flex-end;

            background: #111827;
            color: white;

            border-bottom-right-radius: 4px;
        }

        .chatbot-bot {
            align-self: flex-start;

            background: #f3f4f6;
            color: #111827;

            border-bottom-left-radius: 4px;
        }

        .chatbot-error {
            background: #fef2f2;
            color: #991b1b;
        }

        #chatbot-input-area {
            display: flex;

            gap: 8px;

            padding: 10px;

            border-top:
                1px solid #e5e7eb;

            background: white;
        }

        #chatbot-input {
            flex: 1;

            min-width: 0;

            padding:
                10px 12px;

            border:
                1px solid #d1d5db;

            border-radius: 10px;

            outline: none;

            font-size: 14px;
        }

        #chatbot-input:focus {
            border-color: #6b7280;
        }

        #chatbot-send {
            border: none;

            padding:
                10px 14px;

            border-radius: 10px;

            background: #111827;
            color: white;

            cursor: pointer;

            font-size: 14px;
        }

        #chatbot-send:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }

        .chatbot-loading {
            display: inline-flex;
            gap: 4px;
            align-items: center;
        }

        .chatbot-loading span {
            width: 5px;
            height: 5px;
            border-radius: 50%;
            background: #6b7280;
            animation:
                chatbot-bounce
                1.2s infinite ease-in-out;
        }

        .chatbot-loading span:nth-child(2) {
            animation-delay: 0.15s;
        }

        .chatbot-loading span:nth-child(3) {
            animation-delay: 0.3s;
        }

        @keyframes chatbot-bounce {
            0%, 80%, 100% {
                transform: translateY(0);
            }

            40% {
                transform: translateY(-4px);
            }
        }

        @media (max-width: 480px) {
            #chatbot-button {
                right: 16px;
                bottom: 16px;
            }

            #chatbot-window {
                right: 0;
                bottom: 0;

                width: 100%;
                height: 100%;

                border-radius: 0;
            }
        }
    `;

    document.head.appendChild(style);

    /* ==============================
       UI
       ============================== */

    const button =
        document.createElement("button");

    button.id = "chatbot-button";
    button.type = "button";
    button.setAttribute(
        "aria-label",
        "Open chatbot"
    );

    button.textContent = "💬";

    const windowElement =
        document.createElement("div");

    windowElement.id =
        "chatbot-window";

    windowElement.setAttribute(
        "role",
        "dialog"
    );

    windowElement.innerHTML = `
        <div id="chatbot-header">
            Website Assistant
        </div>

        <div
            id="chatbot-messages"
            aria-live="polite">
        </div>

        <div id="chatbot-input-area">

            <input
                id="chatbot-input"
                type="text"
                autocomplete="off"
                placeholder="Ask something..."
                aria-label="Chat message"
            />

            <button
                id="chatbot-send"
                type="button">
                Send
            </button>

        </div>
    `;

    document.body.appendChild(button);
    document.body.appendChild(windowElement);

    const messagesElement =
        document.getElementById(
            "chatbot-messages"
        );

    const inputElement =
        document.getElementById(
            "chatbot-input"
        );

    const sendButton =
        document.getElementById(
            "chatbot-send"
        );

    /* ==============================
       OPEN / CLOSE
       ============================== */

    button.addEventListener(
        "click",
        function () {

            const isOpen =
                windowElement.style.display ===
                "flex";

            windowElement.style.display =
                isOpen
                    ? "none"
                    : "flex";

            button.setAttribute(
                "aria-expanded",
                String(!isOpen)
            );

            if (!isOpen) {
                inputElement.focus();
            }
        }
    );

    /* ==============================
       ADD MESSAGE
       ============================== */

    function addMessage(
        text,
        type
    ) {
        const message =
            document.createElement("div");

        message.classList.add(
            "chatbot-message"
        );

        if (type === "user") {
            message.classList.add(
                "chatbot-user"
            );
        } else {
            message.classList.add(
                "chatbot-bot"
            );
        }

        message.textContent = text;

        messagesElement.appendChild(
            message
        );

        scrollMessages();

        return message;
    }

    function addLoadingMessage() {
        const message =
            document.createElement("div");

        message.classList.add(
            "chatbot-message",
            "chatbot-bot"
        );

        message.innerHTML = `
            <div class="chatbot-loading">
                <span></span>
                <span></span>
                <span></span>
            </div>
        `;

        messagesElement.appendChild(
            message
        );

        scrollMessages();

        return message;
    }

    function scrollMessages() {
        messagesElement.scrollTop =
            messagesElement.scrollHeight;
    }

    /* ==============================
       SEND MESSAGE
       ============================== */

    async function sendMessage() {

        if (isSending) {
            return;
        }

        const message =
            inputElement.value.trim();

        if (!message) {
            return;
        }

        isSending = true;

        addMessage(
            message,
            "user"
        );

        inputElement.value = "";

        inputElement.disabled = true;
        sendButton.disabled = true;

        const loadingMessage =
            addLoadingMessage();

        try {

            const response =
                await fetch(
                    apiBaseUrl +
                    "/api/chat",
                    {
                        method: "POST",

                        headers: {
                            "Content-Type": "application/json",
                            "X-Chatbot-Public-Key": siteKey
                        },

                        body:
                            JSON.stringify({
                                conversationId:
                                    conversationId,

                                message:
                                    message
                            })
                    }
                );

            const data =
                await response.json();

            loadingMessage.remove();

            if (!response.ok) {

                addErrorMessage(
                    data.message ||
                    "Something went wrong."
                );

                return;
            }

            addMessage(
                data.response ||
                "I couldn't generate a response.",
                "bot"
            );

        } catch (error) {

            console.error(
                "Chatbot error:",
                error
            );

            loadingMessage.remove();

            addErrorMessage(
                "Unable to connect to the chatbot."
            );

        } finally {

            isSending = false;

            inputElement.disabled = false;
            sendButton.disabled = false;

            inputElement.focus();
        }
    }

    function addErrorMessage(text) {

        const message =
            document.createElement("div");

        message.classList.add(
            "chatbot-message",
            "chatbot-bot",
            "chatbot-error"
        );

        message.textContent = text;

        messagesElement.appendChild(
            message
        );

        scrollMessages();
    }

    sendButton.addEventListener(
        "click",
        sendMessage
    );

    inputElement.addEventListener(
        "keydown",
        function (event) {

            if (event.key === "Enter") {

                event.preventDefault();

                sendMessage();
            }
        }
    );

    /* ==============================
       INITIAL MESSAGE
       ============================== */

    addMessage(
        "Hello! How can I help you?",
        "bot"
    );

})();