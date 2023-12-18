package cloudserver.download;

import cloud.PairOfPathnames;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DownloadRequestDecoder extends ReplayingDecoder<String> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)  {
        int serverPathLen = in.readInt();
        String serverPathString = in.readCharSequence(serverPathLen, StandardCharsets.UTF_8).toString();
        int clientPathLen = in.readInt();
        String clientPathString = in.readCharSequence(clientPathLen, StandardCharsets.UTF_8).toString();
        Path serverPath = Paths.get(serverPathString);
        String clientFilePath = clientPathString + serverPath.getFileName();
        out.add(new PairOfPathnames(serverPathString, clientFilePath));
    }
}