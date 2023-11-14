package cloudclient.upload;
import cloud.FileMetadata;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class UploadRequestEncoder extends MessageToByteEncoder<FileMetadata> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    protected void encode(ChannelHandlerContext ctx, FileMetadata msg, ByteBuf out) throws Exception {
        String cmdName = "upload";
        out.writeInt(cmdName.length());
        out.writeCharSequence(cmdName, charset);
        out.writeInt(msg.getNameLen());
        out.writeCharSequence(msg.getPathname(), StandardCharsets.UTF_8);
        out.writeLong(msg.getFileLen());
    }
}