package cloudserver.movecopy;

import cloud.PairOfPathnames;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

public class MoveOrCopyRequestDecoder extends ReplayingDecoder<PairOfPathnames> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int initPathLen = in.readInt();
        String initPath = in.readCharSequence(initPathLen, StandardCharsets.UTF_8).toString();
        int destPathLen = in.readInt();
        String destPath = in.readCharSequence(destPathLen, StandardCharsets.UTF_8).toString();
        out.add(new PairOfPathnames(initPath, destPath + Paths.get(initPath).getFileName()));
    }
}
