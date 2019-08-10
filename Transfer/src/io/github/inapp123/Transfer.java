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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import java.util.Queue;
/**
 * Transfer
 * @author Saber
 */
public class Transfer extends JavaPlugin implements Listener{
    Transfercfgloader cfg;
    @Override
    public void onEnable(){
        cfg = new Transfercfgloader(this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        for(Transfercollection transcolle:cfg.transcolle)
        {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, transcolle, transcolle.starttime, transcolle.detecttime);
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("join")&&(sender instanceof Player)) {
            if(args.length == 0)
                return false;
            if(!Transferutil.onequeuecontain((Player)sender, cfg)){
                if(cfg.iscollectionexist(args[0])){
                    cfg.getcollectionbyname(args[0]).Playerqueue.offer((Player)sender);
                    sender.sendMessage(cfg.please_wait);
                }else{
                    sender.sendMessage(cfg.unknow_queue);
                }
            }else{
                sender.sendMessage(cfg.in_queue);
            }
            return true;
        }else if (cmd.getName().equalsIgnoreCase("leave")&&(sender instanceof Player)) {
            Queue<Player> qtemp = Transferutil.whichqueuecontain((Player)sender, cfg);
            if(qtemp!=null){
                qtemp.remove((Player)sender);
                sender.sendMessage(cfg.leave_queue);
            }else{
                sender.sendMessage(cfg.not_in_queue);
            }
            return true;
        }else if (cmd.getName().equalsIgnoreCase("transreload")) {
            cfg = null;
            this.reloadConfig();
            cfg = new Transfercfgloader(this);
            sender.sendMessage("transfer 匹配插件已重载");
            return true;
        }
        sender.sendMessage(cfg.must_player);
        return false;
    }
    @EventHandler
    public void playerleave(PlayerQuitEvent e){
        Queue<Player> qtemp = Transferutil.whichqueuecontain(e.getPlayer(), cfg);
        if(qtemp!=null)
        {
            qtemp.remove(e.getPlayer());
        }
    }
}
