package cloudserver.upload;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileAcceptor extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bb = (ByteBuf) msg;
        int len = bb.readableBytes();
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = bb.readByte();
            UploadServerHandler.writtenBytes++;
        }
        Path path = Paths.get(UploadServerHandler.pathname);
        if (Files.notExists(path)) {
            Files.write(path, bytes);
        } else {
            Files.write(path, bytes, StandardOpenOption.APPEND);
        }
        if (UploadServerHandler.writtenBytes == UploadServerHandler.fileLen) {
            ctx.writeAndFlush("Downloading complete");
        }
        bb.release();
    }
}