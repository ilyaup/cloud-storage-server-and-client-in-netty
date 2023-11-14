package cloudclient.auth;

import cloudclient.User;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class AuthRequestEncoder extends MessageToByteEncoder<User> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void encode(ChannelHandlerContext ctx, User msg, ByteBuf out) {
        String cmdName = "login";
        out.writeInt(cmdName.length());
        out.writeCharSequence(cmdName, charset);
        out.writeInt(msg.getLogin().length());
        out.writeCharSequence(msg.getLogin(), charset);
        out.writeInt(msg.getPassword().length());
        out.writeCharSequence(msg.getPassword(), charset);
    }
}
