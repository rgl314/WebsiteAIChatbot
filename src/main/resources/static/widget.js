(function () {

    const script =
        document.currentScript ||
        document.querySelector('script[data-site-key]');

    if (!script) {
        console.error('Chatbot: script tag not found.');
        return;
    }

    const siteKey = script.dataset.siteKey;

    if (!siteKey) {
        console.error('Chatbot: data-site-key is missing.');
        return;
    }

    const apiBaseUrl =
        script.dataset.apiUrl ||
        'http://localhost:8080';

    const storageKey =
        'chatbot_conversation_' +
        siteKey;

    let conversationId =
        localStorage.getItem(
            storageKey
        );

    if (!conversationId) {
        conversationId =
            crypto.randomUUID();

        localStorage.setItem(
            storageKey,
            conversationId
        );
    }

    const style = document.createElement('style');

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
            box-shadow: 0 4px 18px rgba(0,0,0,0.25);
        }

        #chatbot-window {
            position: fixed;
            right: 24px;
            bottom: 96px;
            width: 360px;
            height: 520px;
            background: white;
            border: 1px solid #ddd;
            border-radius: 16px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            display: none;
            flex-direction: column;
            overflow: hidden;
            z-index: 999998;
            font-family: Arial, sans-serif;
        }

        #chatbot-header {
            padding: 16px;
            background: #111827;
            color: white;
            font-weight: bold;
        }

        #chatbot-messages {
            flex: 1;
            padding: 16px;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .chatbot-message {
            max-width: 80%;
            padding: 10px 14px;
            border-radius: 12px;
            line-height: 1.4;
            white-space: pre-wrap;
        }

        .chatbot-user {
            align-self: flex-end;
            background: #111827;
            color: white;
        }

        .chatbot-bot {
            align-self: flex-start;
            background: #f1f1f1;
            color: #111;
        }

        #chatbot-input-area {
            display: flex;
            border-top: 1px solid #ddd;
            padding: 10px;
            gap: 8px;
        }

        #chatbot-input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 8px;
            outline: none;
        }

        #chatbot-send {
            padding: 10px 14px;
            border: none;
            border-radius: 8px;
            background: #111827;
            color: white;
            cursor: pointer;
        }
    `;

    document.head.appendChild(style);

    const button = document.createElement('button');

    button.id = 'chatbot-button';
    button.innerHTML = '💬';

    const windowElement = document.createElement('div');

    windowElement.id = 'chatbot-window';

    windowElement.innerHTML = `
        <div id="chatbot-header">
            Website Assistant
        </div>

        <div id="chatbot-messages"></div>

        <div id="chatbot-input-area">
            <input
                id="chatbot-input"
                type="text"
                placeholder="Ask something..."
            />

            <button id="chatbot-send">
                Send
            </button>
        </div>
    `;

    document.body.appendChild(button);
    document.body.appendChild(windowElement);

    const messagesElement =
        document.getElementById(
            'chatbot-messages'
        );

    const inputElement =
        document.getElementById(
            'chatbot-input'
        );

    const sendButton =
        document.getElementById(
            'chatbot-send'
        );

    button.addEventListener(
        'click',
        function () {

            const isOpen =
                windowElement.style.display ===
                'flex';

            windowElement.style.display =
                isOpen ? 'none' : 'flex';

            if (!isOpen) {
                inputElement.focus();
            }
        }
    );

    function addMessage(
        text,
        type
    ) {

        const message =
            document.createElement('div');

        message.className =
            'chatbot-message ' +
            (type === 'user'
                ? 'chatbot-user'
                : 'chatbot-bot');

        message.textContent = text;

        messagesElement.appendChild(message);

        messagesElement.scrollTop =
            messagesElement.scrollHeight;

        return message;
    }

    async function sendMessage() {

        const message =
            inputElement.value.trim();

        if (!message) {
            return;
        }

        addMessage(message, 'user');

        inputElement.value = '';

        const loadingMessage =
            addMessage(
                'Thinking...',
                'bot'
            );

        sendButton.disabled = true;

        try {

            const response =
                await fetch(
                    apiBaseUrl +
                    '/api/chat',
                    {
                        method: 'POST',

                        headers: {
                            'Content-Type':
                                'application/json'
                        },

                        body: JSON.stringify({
                            publicKey: siteKey,
                            conversationId:
                                conversationId,
                            message: message
                        })
                    }
                );

            if (!response.ok) {
                throw new Error(
                    `HTTP ${response.status}`
                );
            }

            const data =
                await response.json();

            loadingMessage.textContent =
                data.response ||
                'Sorry, I could not generate a response.';

        } catch (error) {

            console.error(
                'Chatbot error:',
                error
            );

            loadingMessage.textContent =
                'Sorry, something went wrong.';

        } finally {

            sendButton.disabled = false;

            inputElement.focus();
        }
    }

    sendButton.addEventListener(
        'click',
        sendMessage
    );

    inputElement.addEventListener(
        'keydown',
        function (event) {

            if (event.key === 'Enter') {
                sendMessage();
            }

        }
    );

    addMessage(
        'Hello! How can I help you?',
        'bot'
    );

})();