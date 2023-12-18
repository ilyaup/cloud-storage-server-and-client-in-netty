package cloudclient.movecopy;

import cloud.PairOfPathnames;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CopyRequestEncoder extends MessageToByteEncoder<PairOfPathnames> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void encode(ChannelHandlerContext ctx, PairOfPathnames msg, ByteBuf out) throws Exception {
        String cmdName = "copy";
        out.writeInt(cmdName.length());
        out.writeCharSequence(cmdName, charset);
        out.writeInt(msg.firstPathname().length());
        out.writeCharSequence(msg.firstPathname(), charset);
        out.writeInt(msg.secondPathname().length());
        out.writeCharSequence(msg.secondPathname(), charset);
    }
}
