package cloud;

import cloud.FileMetadata;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class MetadataEncoder extends MessageToByteEncoder<FileMetadata> {
    @Override
    protected void encode(ChannelHandlerContext ctx, FileMetadata msg, ByteBuf out) {
        out.writeInt(msg.getNameLen());
        out.writeCharSequence(msg.getPathname(), StandardCharsets.UTF_8);
        out.writeLong(msg.getFileLen());
    }
}
