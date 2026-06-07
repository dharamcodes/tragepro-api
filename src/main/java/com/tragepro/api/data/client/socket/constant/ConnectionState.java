package com.tragepro.api.data.client.socket.constant;

/**
 * Connection states for tracking WebSocket client lifecycle.
 */
public enum ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING
}
