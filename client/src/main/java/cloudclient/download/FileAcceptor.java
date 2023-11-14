package cloudclient.download;

import cloud.FileMetadata;
import cloudclient.CloudClientHandler;
import cloudclient.PipelineStateManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileAcceptor extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bb = (ByteBuf) msg;
        int len = bb.readableBytes();
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = bb.readByte();
            MetadataAcceptor.writtenBytes++;
        }
        Path path = Paths.get(MetadataAcceptor.pathname);
        if (Files.notExists(path)) {
            Files.write(path, bytes);
        } else {
            Files.write(path, bytes, StandardOpenOption.APPEND);
        }
        bb.release();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        if (MetadataAcceptor.writtenBytes == MetadataAcceptor.fileLen) {
            ctx.fireChannelRead("Downloading complete");
        }
    }
}