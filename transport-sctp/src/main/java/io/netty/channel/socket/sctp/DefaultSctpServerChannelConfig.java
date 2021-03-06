/*
* Copyright 2011 The Netty Project
*
* The Netty Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package io.netty.channel.socket.sctp;

import com.sun.nio.sctp.SctpServerChannel;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelConfig;
import io.netty.util.NetUtil;

import java.io.IOException;
import java.util.Map;

import static com.sun.nio.sctp.SctpStandardSocketOptions.*;

/**
 * The default {@link SctpServerChannelConfig} implementation for SCTP.
 */
public class DefaultSctpServerChannelConfig extends DefaultChannelConfig implements SctpServerChannelConfig {

    private final SctpServerChannel javaChannel;
    private volatile int backlog = NetUtil.SOMAXCONN;

    /**
     * Creates a new instance.
     */
    public DefaultSctpServerChannelConfig(
            io.netty.channel.socket.sctp.SctpServerChannel channel, SctpServerChannel javaChannel) {
        super(channel);
        if (javaChannel == null) {
            throw new NullPointerException("javaChannel");
        }
        this.javaChannel = javaChannel;
    }

    @Override
    public Map<ChannelOption<?>, Object> getOptions() {
        return getOptions(
                super.getOptions(),
                ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SCTP_INIT_MAXSTREAMS);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOption(ChannelOption<T> option) {
        if (option == ChannelOption.SO_RCVBUF) {
            return (T) Integer.valueOf(getReceiveBufferSize());
        }
        if (option == ChannelOption.SO_SNDBUF) {
            return (T) Integer.valueOf(getSendBufferSize());
        }
        return super.getOption(option);
    }

    @Override
    public <T> boolean setOption(ChannelOption<T> option, T value) {
        validate(option, value);

        if (option == ChannelOption.SO_RCVBUF) {
            setReceiveBufferSize((Integer) value);
        } else if (option == ChannelOption.SO_SNDBUF) {
            setSendBufferSize((Integer) value);
        } else if (option == ChannelOption.SCTP_INIT_MAXSTREAMS) {
            setInitMaxStreams((InitMaxStreams) value);
        } else {
            return super.setOption(option, value);
        }

        return true;
    }

    @Override
    public int getSendBufferSize() {
        try {
            return javaChannel.getOption(SO_SNDBUF);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override
    public SctpServerChannelConfig setSendBufferSize(int sendBufferSize) {
        try {
            javaChannel.setOption(SO_SNDBUF, sendBufferSize);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }

    @Override
    public int getReceiveBufferSize() {
        try {
            return javaChannel.getOption(SO_RCVBUF);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override
    public SctpServerChannelConfig setReceiveBufferSize(int receiveBufferSize) {
        try {
            javaChannel.setOption(SO_RCVBUF, receiveBufferSize);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }

    @Override
    public InitMaxStreams getInitMaxStreams() {
        try {
            return javaChannel.getOption(SCTP_INIT_MAXSTREAMS);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
    }

    @Override
    public SctpServerChannelConfig setInitMaxStreams(InitMaxStreams initMaxStreams) {
        try {
            javaChannel.setOption(SCTP_INIT_MAXSTREAMS, initMaxStreams);
        } catch (IOException e) {
            throw new ChannelException(e);
        }
        return this;
    }

    @Override
    public int getBacklog() {
        return backlog;
    }

    @Override
    public SctpServerChannelConfig setBacklog(int backlog) {
        if (backlog < 0) {
            throw new IllegalArgumentException("backlog: " + backlog);
        }
        this.backlog = backlog;
        return this;
    }

    @Override
    public SctpServerChannelConfig setWriteSpinCount(int writeSpinCount) {
        return (SctpServerChannelConfig) super.setWriteSpinCount(writeSpinCount);
    }

    @Override
    public SctpServerChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        return (SctpServerChannelConfig) super.setConnectTimeoutMillis(connectTimeoutMillis);
    }

    @Override
    public SctpServerChannelConfig setAllocator(ByteBufAllocator allocator) {
        return (SctpServerChannelConfig) super.setAllocator(allocator);
    }

    @Override
    public SctpServerChannelConfig setAutoRead(boolean autoRead) {
        return (SctpServerChannelConfig) super.setAutoRead(autoRead);
    }
}
