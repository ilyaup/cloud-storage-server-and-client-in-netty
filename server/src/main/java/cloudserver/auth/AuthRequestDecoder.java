package cloudserver.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AuthRequestDecoder extends ReplayingDecoder<User> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        User user = new User();
        int loginLen = in.readInt();
        user.setLogin(in.readCharSequence(loginLen, charset).toString());
        int passwordLen = in.readInt();
        user.setPassword(in.readCharSequence(passwordLen, charset).toString());
        out.add(user);
    }
}
