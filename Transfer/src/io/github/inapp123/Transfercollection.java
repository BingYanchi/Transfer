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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Transfer集合
 * 用来表示一个队列用的对象的类. <br>
 * 构造函数: <br>
 * config 用bukkit api获取到的服务器配置类<br>
 * node 定义该集合的节点 <br>
 * plugin transfer插件 <br>
 * @author Saber
 */
public class Transfercollection implements Runnable{
    private final Plugin plugin;
    public Queue<Player> Playerqueue = new LinkedList<>();
    public String allowedmotd;
    public int timeout;
    public int detecttime;
    public int starttime;
    public Transferserver server[];
    Transfercollection(FileConfiguration config,String node,Plugin thisplugin){
        server = new Transferserver[config.getIntegerList(node+".server.port").size()];
        for(int i=0;i<server.length;i++){
            server[i]=new Transferserver(i,
                config.getStringList(node+".server.servername").get(i),
                config.getIntegerList(node+".server.port").get(i),
                config.getIntegerList(node+".server.maxplayer").get(i));
        }
        timeout = config.getInt(node+".timeout");
        detecttime = config.getInt(node+".detecttime");
        starttime = config.getInt(node+".starttime");
        allowedmotd = config.getString(node+".allowedmotd");
        plugin = thisplugin;
    }
    
    @Override
    public void run() {
        if(!Playerqueue.isEmpty()){//当玩家队列不为空的时候
            Queue<Transferserver> asq = Transferutil.SortOLP(Transferutil.GetASQ(this));//获取可匹配服务器,后按照人数从多到少排序
            while(!asq.isEmpty() && !Playerqueue.isEmpty()){//当玩家和可匹配服务器都不为0时
                Transferserver serv = asq.poll(); //提取一台服务器
                int playerqueuecount = Playerqueue.size();
                for(int i=1;i<=Math.min(serv.getmaxplayer()-serv.onlineplayer, playerqueuecount);i++){
                    Transferutil.sendplayer(serv,Playerqueue.remove(),plugin);
                }
            }
        }
    }
}
