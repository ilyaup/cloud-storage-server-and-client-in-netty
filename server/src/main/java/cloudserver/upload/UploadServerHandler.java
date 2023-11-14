package cloudserver.upload;

import cloud.FileMetadata;
import cloudserver.HandlersClearer;
import cloudserver.PortUnificationServerHandler;
import io.netty.channel.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UploadServerHandler extends ChannelInboundHandlerAdapter {

    public static String pathname = "";

    public static int fileLen = 0;
    public static int writtenBytes = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FileMetadata fileMetadata = (FileMetadata) msg;
        pathname = fileMetadata.getPathname();
        Files.deleteIfExists(Paths.get(pathname));
        fileLen = (int) fileMetadata.getFileLen();
        writtenBytes = 0;
        HandlersClearer.clearHandlers(ctx);
        PortUnificationServerHandler.setupPipeline.get("upload1").accept(ctx);
        ctx.pipeline().addFirst("clearHandlers", new HandlersClearer());
    }

}
