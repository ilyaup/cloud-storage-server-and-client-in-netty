package cloudserver.movecopy;

import cloud.PairOfPathnames;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CopyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PairOfPathnames pairOfPathnames = (PairOfPathnames) msg;
        Files.copy(Paths.get(pairOfPathnames.firstPathname()), Paths.get(pairOfPathnames.secondPathname()),
                StandardCopyOption.REPLACE_EXISTING);
        ctx.writeAndFlush("Copied");
    }
}
