package cloudclient;

import cloud.FileMetadata;
import cloud.PairOfPathnames;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.stream.ChunkedFile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class CloudClientHandler extends ChannelInboundHandlerAdapter {

    public static final Map<String, BiConsumer<String[], ChannelHandlerContext>> runCmd = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    static {
        runCmd.put("login", (cmd, ctx) -> {
            ctx.writeAndFlush(new User(cmd[1], cmd[2]));
        });
        runCmd.put("download", (cmd, ctx) -> {
            ctx.writeAndFlush(new PairOfPathnames(cmd[1], cmd[2]));
        });
        runCmd.put("move", (cmd, ctx) -> {
            ctx.writeAndFlush(new PairOfPathnames(cmd[1], cmd[2]));
        });
        runCmd.put("copy", (cmd, ctx) -> {
            ctx.writeAndFlush(new PairOfPathnames(cmd[1], cmd[2]));
        });
        runCmd.put("upload", (cmd, ctx) -> {
            try {
                RandomAccessFile ref = new RandomAccessFile(cmd[2], "r");
                String pathname = cmd[1] + Paths.get(cmd[2]).getFileName();
                ctx.writeAndFlush(new FileMetadata(pathname.length(), pathname, ref.length()));
                PipelineStateManager.setupPipeline("upload1", ctx);
                Thread.sleep(1000);
                ctx.writeAndFlush(new ChunkedFile(ref));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                ctx.flush();
            }
        });
    }

    private void clientTerminal(ChannelHandlerContext ctx) {
        String[] cmd = scanner.nextLine().split(" ");
        if (cmd.length != 3) {
            System.out.println("Should be 1 cmd and 2 arguments. Enter a command from README.md and two arguments:");
            clientTerminal(ctx);
            return;
        }
        if (runCmd.get(cmd[0]) == null) {
            System.out.println("No such command. Enter a command from README.md:");
            clientTerminal(ctx);
            return;
        }
        PipelineStateManager.setupPipeline(cmd[0], ctx);
        runCmd.get(cmd[0]).accept(cmd, ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        clientTerminal(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println((String) msg);
        clientTerminal(ctx);
    }

}
