package com.swayam.demo.rmi.server.core.jini.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jini.jeri.RequestDispatcher;
import net.jini.jeri.ServerEndpoint.ListenCookie;
import net.jini.jeri.ServerEndpoint.ListenHandle;

import com.swayam.demo.rmi.shared.jini.IOStreamProvider;

/**
 * ListenHandle implementation: represents a listen operation.
 **/
class ListenHandleImpl implements ListenHandle {

    private static final Logger logger = Logger.getLogger("net.jini.jeri.http.server");

    private final RequestDispatcher requestDispatcher;
    private final ServerSocket serverSocket;
    private final ListenCookie cookie;

    private final Executor threadPool;

    ListenHandleImpl(RequestDispatcher requestDispatcher, ServerSocket serverSocket, ListenCookie cookie) {
        this.requestDispatcher = requestDispatcher;
        this.serverSocket = serverSocket;
        this.cookie = cookie;

        threadPool = Executors.newFixedThreadPool(1);
    }

    /**
     * Starts the accept loop.
     **/
    void startAccepting() {
        threadPool.execute(new Runnable() {
            public void run() {
                try {
                    executeAcceptLoop();
                } finally {
                    /*
                     * The accept loop is only started once, so after no more
                     * socket accepts will occur, ensure that the server socket
                     * is no longer listening.
                     */
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * Executes the accept loop.
     **/
    private void executeAcceptLoop() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "accepted socket {0} from server socket {1}", new Object[] { socket, serverSocket });
            }

            IOStreamProvider ioStreamProvider = new SocketIOStreamProvider(socket);

            try {
                new HttpServerConnection(ioStreamProvider, requestDispatcher).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Stops this listen operation.
     **/
    public void close() {

    }

    /**
     * Returns a cookie to identify this listen operation.
     **/
    public ListenCookie getCookie() {
        return cookie;
    }

    public String toString() {
        return "HttpServerEndpoint.LH[" + serverSocket + "]";
    }

}