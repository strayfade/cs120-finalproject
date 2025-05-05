const WebSocket = require('ws');
const port = 2300
const wss = new WebSocket.Server({ port: port });

const { log, logColors } = require('./log')

// Keep track of connected clients
const clients = new Set();
wss.on('connection', (ws) => {
    clients.add(ws);
    log(`New client connected. Total clients: ${clients.size}`);

    // Listen for messages
    ws.on('message', (message) => {

        // Sends message to other clients
        for (let client of clients) {
            if (client !== ws && client.readyState === WebSocket.OPEN) {
                client.send(message);
            }
        }
    });

    // Handle client disconnect
    ws.on('close', () => {
        clients.delete(ws);
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
