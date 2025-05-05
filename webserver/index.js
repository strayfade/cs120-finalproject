const WebSocket = require('ws');
const port = 2300
const wss = new WebSocket.Server({ port: port });

const { log, logColors } = require('./log')

const generateUsername = (length = 16) => {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
}

// Keep track of connected clients
const clients = [];
wss.on('connection', (ws) => {
    clients.push({
        username: generateUsername(),
        socket: ws
    });
    log(`New client connected. Total clients: ${clients.size}`);

    // Listen for messages
    ws.on('message', (message) => {

        // Sends message to other clients
        for (let client of clients) {
            if (client && client !== ws && client.readyState === WebSocket.OPEN) {
                client.send(message);
            }
        }
    });

    // Handle client disconnect
    ws.on('close', () => {

        // Remove user from clients array
        for (let client of clients) {
            if (client.socket == ws)
                clients.splice(clients.indexOf(client), 1)
        }
        log(`Client disconnected. Total clients: ${clients.size}`);
    });

    // Handle errors
    ws.on('error', (error) => {
        log(`WebSocket error: ${error}`, logColors.ErrorVisible);
    });
});

wss.on('listening', () => {
    log(`WebSocket server is running on port ${port}`);
})
