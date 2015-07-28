package com.github.lookout.metrics.agent;

/**
 * Created by rkuris on 12/19/14.
 */
public class HostPortInterval {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8125;
    public static final int DEFAULT_INTERVAL = 10;

    private final String host;
    private final int port;
    private final int interval;
    private final String prefix;

    public HostPortInterval(final String hostPortIntervalPrefix) {
        if (hostPortIntervalPrefix == null || hostPortIntervalPrefix.isEmpty()) {
            this.host = DEFAULT_HOST;
            this.port = DEFAULT_PORT;
            this.interval = DEFAULT_INTERVAL;
            this.prefix = null;
            return;
        }

        String hostPortInterval;
        int prefixOffset = hostPortIntervalPrefix.lastIndexOf('/');
        if (prefixOffset != -1) {
            this.prefix = hostPortIntervalPrefix.substring(prefixOffset + 1);
            hostPortInterval = hostPortIntervalPrefix.substring(0, prefixOffset);
        } else {
            this.prefix = null;
            hostPortInterval = hostPortIntervalPrefix;
        }

        int intervalOffset = hostPortInterval.lastIndexOf('@');
        final String hostPort;
        if (intervalOffset == -1) {
            this.interval = DEFAULT_INTERVAL;
            hostPort = hostPortInterval;
        } else {
            this.interval = Integer.parseInt(hostPortInterval.substring(intervalOffset + 1));
            hostPort = hostPortInterval.substring(0, intervalOffset);
        }
        int colonOffset = hostPort.lastIndexOf(':');
        if (colonOffset == -1 || hostPort.endsWith("]")) {
            this.host = stripBrackets(hostPort);
            this.port = DEFAULT_PORT;
        } else {
            final String hostPart = hostPort.substring(0, colonOffset);
            final String portPart = hostPort.substring(colonOffset + 1);
            this.host = stripBrackets(hostPart);
            this.port = Integer.parseInt(portPart);
        }

    }

    private String stripBrackets(final String source) {
        int sourceLength = source.length();
        if (sourceLength > 2 && source.charAt(0) == '[' && source.charAt(sourceLength - 1) == ']') {
            return source.substring(1, sourceLength - 1);
        }
        return source;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getInterval() {
        return interval;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public String toString() {
        return String.format("host %s port %d every %d seconds", host, port, interval);
    }
}
