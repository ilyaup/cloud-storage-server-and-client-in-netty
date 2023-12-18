package cloudclient;

import cloud.MetadataDecoder;
import cloud.PipelineState;
import cloudclient.auth.AuthRequestEncoder;
import cloudclient.download.DownloadRequestEncoder;
import cloudclient.download.FileAcceptor;
import cloudclient.download.MetadataAcceptor;
import cloudclient.movecopy.CopyRequestEncoder;
import cloudclient.movecopy.MoveRequestEncoder;
import cloudclient.upload.UploadRequestEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PipelineStateManager {

    public static PipelineState currentState = PipelineState.LOGIN; // Init state is LOGIN

    public static final String BASE_HANDLER_NAME = "cloudClientHandler";
    public static final Map<PipelineState, List<ChannelHandler>> handlersByState = new HashMap<>(); // not refactored map

    static {
        List<ChannelHandler> handlers = new ArrayList<>(2);
        handlers.add(new AuthRequestEncoder());
        handlers.add(new TextResponseDecoder());
        handlersByState.put(PipelineState.LOGIN, handlers);
    }

    private static final Map<PipelineState, Consumer<ChannelHandlerContext>> setupHandlersByState = new HashMap<>();

    static {
        setupHandlersByState.put(PipelineState.LOGIN, (ctx) -> {
            ctx.pipeline().addFirst(new TextResponseDecoder());
            ctx.pipeline().addFirst(new AuthRequestEncoder());
        });
        setupHandlersByState.put(PipelineState.DOWNLOAD, (ctx) -> {
            ctx.pipeline().addFirst(new MetadataAcceptor());
            ctx.pipeline().addFirst(new MetadataDecoder());
            ctx.pipeline().addFirst(new DownloadRequestEncoder());
        });
        setupHandlersByState.put(PipelineState.DOWNLOAD_PROCESSING, (ctx) -> {
            ctx.pipeline().addFirst(new ChunkedWriteHandler());
            ctx.pipeline().addFirst(new FileAcceptor());
        });
        setupHandlersByState.put(PipelineState.UPLOAD, (ctx) -> {
            ctx.pipeline().addFirst(new UploadRequestEncoder());
        });
        setupHandlersByState.put(PipelineState.UPLOAD_PROCESSING, (ctx) -> {
            ctx.pipeline().addFirst(new ChunkedWriteHandler());
            ctx.pipeline().addFirst(new TextResponseDecoder());
        });
        setupHandlersByState.put(PipelineState.MOVE, (ctx) -> {
            ctx.pipeline().addFirst(new TextResponseDecoder());
            ctx.pipeline().addFirst(new MoveRequestEncoder());
        });
        setupHandlersByState.put(PipelineState.COPY, (ctx) -> {
            ctx.pipeline().addFirst(new TextResponseDecoder());
            ctx.pipeline().addFirst(new CopyRequestEncoder());
        });
    }

    public static void setupPipeline(String cmdName, ChannelHandlerContext ctx) {
        PipelineState newState = PipelineState.stateFromCmdName(cmdName);
        if (newState != currentState) {
            for (Map.Entry<String, ChannelHandler> channelHandlerEntry : ctx.pipeline().toMap().entrySet()) {
                if (!channelHandlerEntry.getKey().equals(BASE_HANDLER_NAME)) {
                    ctx.pipeline().remove(channelHandlerEntry.getValue());
                }
            }
            setupHandlersByState.get(newState).accept(ctx);
            currentState = newState;
        }
    }

    public static ChannelHandler initPipeline() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                for (ChannelHandler handler : handlersByState
                        .get(currentState)) {
                    ch.pipeline().addLast(handler.toString(), handler);
                }
                ch.pipeline().addLast(BASE_HANDLER_NAME, new CloudClientHandler());
            }
        };
    }
}