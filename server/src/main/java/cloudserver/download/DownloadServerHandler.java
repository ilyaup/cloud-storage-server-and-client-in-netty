package cloudserver.download;

import cloud.FileMetadata;
import cloud.PairOfPathnames;
import cloudserver.HandlersClearer;
import cloudserver.PortUnificationServerHandler;
import io.netty.channel.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DownloadServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        PairOfPathnames pairOfPathnames = (PairOfPathnames) msg;
        RandomAccessFile ref = new RandomAccessFile(pairOfPathnames.firstPathname(), "r");
        PortUnificationServerHandler.excludedForClearHandlers.add(this);
        ctx.writeAndFlush(new FileMetadata(pairOfPathnames.secondPathname().length(), pairOfPathnames.secondPathname(), ref.length()));
        ctx.pipeline().addFirst(new ChunkedWriteHandler());
        ctx.pipeline().addFirst("clearHandlers", new HandlersClearer());
        ctx.writeAndFlush(new ChunkedFile(ref));
    }
}