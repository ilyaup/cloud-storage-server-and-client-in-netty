package cloud;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MetadataDecoder extends ReplayingDecoder<FileMetadata> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int nameLen = in.readInt();
        String pathname = in.readCharSequence(nameLen, StandardCharsets.UTF_8).toString();
        long fileLen = in.readLong();
        out.add(new FileMetadata(nameLen, pathname, fileLen));
    }
}
