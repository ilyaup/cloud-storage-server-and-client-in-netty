package cloudclient.download;

import cloud.FileMetadata;
import cloudclient.PipelineStateManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MetadataAcceptor extends ChannelInboundHandlerAdapter {

    public static String pathname = "";
    public static int fileLen;
    public static int writtenBytes;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PipelineStateManager.setupPipeline("download_processing", ctx);
        FileMetadata fileMetadata = (FileMetadata) msg;
        pathname = fileMetadata.getPathname();
        Files.deleteIfExists(Paths.get(pathname));
        fileLen = (int) fileMetadata.getFileLen();
        writtenBytes = 0;
    }
}
