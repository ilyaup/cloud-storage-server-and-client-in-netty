package cloudserver;

import cloud.MetadataDecoder;
import cloudserver.auth.AuthRequestDecoder;
import cloudserver.auth.AuthServerHandler;
import cloudserver.download.DownloadRequestDecoder;
import cloudserver.download.DownloadServerHandler;
import cloud.MetadataEncoder;
import cloudserver.movecopy.CopyHandler;
import cloudserver.movecopy.MoveHandler;
import cloudserver.movecopy.MoveOrCopyRequestDecoder;
import cloudserver.upload.FileAcceptor;
import cloudserver.upload.UploadServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PortUnificationServerHandler extends ByteToMessageDecoder {

    public static List<ChannelHandler> excludedForClearHandlers = new ArrayList<>();

    public static final Map<String, Consumer<ChannelHandlerContext>> setupPipeline = new HashMap<>();
    static {
        setupPipeline.put("login", (ctx) -> {
            ctx.pipeline().addLast(new ResponseEncoder(), new AuthRequestDecoder(), new AuthServerHandler());
        });
        setupPipeline.put("download", (ctx) -> {
            ctx.pipeline().addLast("metadataEncoder", new MetadataEncoder());
            ctx.pipeline().addLast(new DownloadRequestDecoder(), new DownloadServerHandler());
        });
        setupPipeline.put("upload", (ctx) -> {
            ctx.pipeline().addLast(new MetadataDecoder());
            ctx.pipeline().addLast("uploadServerHandler", new UploadServerHandler());
        });
        setupPipeline.put("upload_processing", (ctx) -> {
            ctx.pipeline().addLast(new ResponseEncoder());
            ctx.pipeline().addLast(new FileAcceptor());
            ctx.pipeline().addLast("chunkedWriteHandler", new ChunkedWriteHandler());
        });
        setupPipeline.put("move", (ctx) -> {
            ctx.pipeline().addLast(new ResponseEncoder(), new MoveOrCopyRequestDecoder(), new MoveHandler());
        });
        setupPipeline.put("copy", (ctx) -> {
            ctx.pipeline().addLast(new ResponseEncoder(), new MoveOrCopyRequestDecoder(), new CopyHandler());
        });
    }

    private final static int INT_SIZE = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < INT_SIZE) {
            return;
        }
        int cmdNameLen = in.readInt();
        if (in.readableBytes() < cmdNameLen) {
            return;
        }
        String cmdName = in.readCharSequence(cmdNameLen, StandardCharsets.UTF_8).toString();
        ctx.pipeline().addLast("clearHandlers", new HandlersClearer());
        setupPipeline.get(cmdName).accept(ctx);
        ctx.pipeline().remove(this);
    }
}
