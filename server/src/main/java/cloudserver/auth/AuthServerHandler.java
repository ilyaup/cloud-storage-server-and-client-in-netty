package cloudserver.auth;

import io.netty.channel.*;

public class AuthServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        User user = (User) msg;
        String login = user.getLogin();
        String password = user.getPassword();
        String response;
        if (Users.registeredUsers.get(login) == null) {
            response = "No such user: " + login;
        } else if (Users.loginsOfLoggedInUsers.contains(login)) {
            response = "You are already logged in.";
        }  else if (Users.userLogInAttempts.get(login) <= 0) {
            response = "You have tried too many times. Access is denied.";
        } else if (!Users.registeredUsers.get(login).equals(password)) {
            Users.userLogInAttempts.put(login, Users.userLogInAttempts.get(login) - 1);
            response = "Invalid password. Attempts left: " + Users.userLogInAttempts.get(login);
        } else {
            Users.loginsOfLoggedInUsers.add(login);
            Users.userLogInAttempts.put(login, 3);
            response = "User " + login + " just have logged in successfully!";
        }
        ctx.writeAndFlush(response);
    }

}
