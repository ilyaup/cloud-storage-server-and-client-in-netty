package cloudserver;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class HandlersClearer extends ChannelOutboundHandlerAdapter {

    public static void clearHandlers(ChannelHandlerContext ctx) {
        for (ChannelHandler handlerEntry : ctx.pipeline().toMap().values()) {
            if (!PortUnificationServerHandler.excludedForClearHandlers.contains(handlerEntry)) {
                ctx.pipeline().remove(handlerEntry);
            }
        }
        PortUnificationServerHandler.excludedForClearHandlers.clear();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise);
        clearHandlers(ctx);
        ctx.pipeline().addLast("portUnificationServerHandler", new PortUnificationServerHandler());
    }
}
