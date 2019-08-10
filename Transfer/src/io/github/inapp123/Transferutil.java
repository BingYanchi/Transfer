/*
 * The MIT License
 *
 * Copyright 2018 Saber.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.inapp123;

import org.bukkit.Bukkit;
import ch.jamiete.mcping.MinecraftPing;
import ch.jamiete.mcping.MinecraftPingOptions;
import ch.jamiete.mcping.MinecraftPingReply;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
/**
 * Transfer插件工具包
 * @author Saber
 */
public class Transferutil {

    /**
     * 获取可匹配服务器列表
     * @param transcolle transfercollection对象
     * @return 一个队列(Queue)对象，包含可以匹配的服务器Transferserver对象。
     */
    public static Queue<Transferserver> GetASQ(Transfercollection transcolle){
        Queue<Transferserver> queue = new LinkedList<>();
        for(Transferserver server:transcolle.server)
        { 
           MinecraftPingReply data = doMinecraftPing(server.getport(),transcolle.timeout);
           if(data!=null){
               server.onlineplayer = data.getPlayers().getOnline();
               if(transcolle.allowedmotd.equals(data.getDescription().getText()) && data.getPlayers().getOnline()<server.getmaxplayer())
               {
                    queue.offer(server);
               }
           }
        }
        return queue;
    }

    /**
     * 在异步线程中进行一次Minecraft Ping操作。
     * @param port 端口
     * @param timeout 超时时长
     * @return MinecraftPingReply对象
     */
    public static MinecraftPingReply doMinecraftPing(int port,int timeout){     
        final ExecutorService exec = Executors.newFixedThreadPool(1);
        MinecraftPingReply tempmcpr = null;
        Callable<MinecraftPingReply> call = new Callable<MinecraftPingReply>() {  
            @Override
            public MinecraftPingReply call() throws Exception {  
                return new MinecraftPing().getPing(new MinecraftPingOptions().setHostname("127.0.0.1").setPort(port).setTimeout(timeout));
            }  
        };  
        try {  
            Future<MinecraftPingReply> future = exec.submit(call);
            tempmcpr = future.get(timeout+10, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {  
            Bukkit.getPluginManager().getPlugin("transfer").getLogger().log(Level.SEVERE, "端口:{0} 处理超时!{1}", new Object[]{port,ex.toString()});
        } catch (Exception e) {  
            Bukkit.getPluginManager().getPlugin("transfer").getLogger().log(Level.SEVERE, "端口:{0} 处理失败!{1}", new Object[]{port,e.toString()});
        } 
        // 关闭线程池  
        exec.shutdown();
        return tempmcpr;
    }

    /**
     * 根据在线人数多少排序队列
     * @param nsqueue 需要排序的队列
     * @return 一个已排序的队列
     */
    public static Queue<Transferserver> SortOLP(Queue<Transferserver> nsqueue){
        List<Transferserver> temp = (List<Transferserver>)nsqueue;
        temp.sort(Comparator.comparing(Transferserver::getonlineplayer));
        Collections.reverse(temp);
        return (Queue<Transferserver>)temp;
    }
     /**
     * 发送玩家到指定的服务器
     * @param loc Transferserver服务器对象
     * @param p Player对象，命令执行者
     * @param plugin Transfer插件对象
     */
    public static void sendplayer(Transferserver loc,Player p,Plugin plugin){
        plugin.getLogger().log(Level.INFO, "Sending {0} to server {1} !", new Object[]{p.getDisplayName(), loc.getname()});
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(loc.getname()); // Target Server
        } catch (IOException e) {}
        p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }

    /**
     * 某个玩家是否存在于任何一个队列中
     * @param p 玩家实例
     * @param cfg Transfercfgloader对象
     * @return true/false
     */
    public static boolean onequeuecontain(Player p,Transfercfgloader cfg){
        for(Transfercollection transcolle:cfg.transcolle){
            if(transcolle.Playerqueue.contains(p)){
                return true;
            }
        }
        return false;
    }

    /**
     * 这个玩家存在于哪个队列当中?
     * @param p 玩家实例
     * @param cfg Transfercfgloader对象
     * @return 队列对象
     */
    public static Queue<Player> whichqueuecontain(Player p,Transfercfgloader cfg){
        for(Transfercollection transcolle:cfg.transcolle){
            if(transcolle.Playerqueue.contains(p)){
                return transcolle.Playerqueue;
            }
        }
        return null;
    }
}
