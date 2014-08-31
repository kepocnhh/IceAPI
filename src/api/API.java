package api;

import ice.BaseMessage;
import ice.DataCass;
import ice.DataForRecord;
import ice.Itog;
import ice.forget;
import ice.login;
import ice.ping;
import ice.user;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class API
{
    static public String accpath;
    static public String logpath;
    static public String version;
    static public String toreg;
    static public String StringsConfigFile;
    
    static public void SetProp(String a,String t,String l,String v,String scf)
    {
        accpath=a;
        logpath=l;
        version=v;
        toreg=t;
        StringsConfigFile=scf;
    }
    
    static public user Messaging(login bm) throws IOException, ClassNotFoundException
    {
        user u = Find_user(accpath,bm.get_log());
        if(u != null)
        {
            if(u.GetPass().equalsIgnoreCase(bm.get_pass()))
            {
                return u;   
            }
        }
        return null;
    }
    static public ping Messaging(ping bm) throws IOException, ClassNotFoundException
    {
        if (bm.GetVersion().equals(version))
        {
            return bm;
        }
        else
        {
            return null;
        }
    }
    static public user Messaging(user bm) throws IOException, ClassNotFoundException
    {
        bm = Find_user(accpath, bm.GetMail());
        if (bm!=null)
        {
            AddMessage(bm, toreg);
        }
            return bm;
    }
    static public user Messaging(forget bm) throws IOException, ClassNotFoundException
    {
        user u = Find_user(accpath, bm.log);
        return u;
    }
    static public user Find_user(String path,String mail) throws IOException, ClassNotFoundException
    {
        File f = new File(path);
            if (!f.exists())
            {
                return null;
            }
        List<BaseMessage> loglist =  GetBM_List(path);
            if (loglist != null)
            {
                for (BaseMessage bm : loglist)
                {
                    user tmp = (user) bm;
                    if (tmp.GetMail().equalsIgnoreCase(mail))//mail is used
                    {
                        return tmp;
                    }
                }
            }
        return null;
    }
    static public Itog Find_Itog(String path, String mail) throws IOException, ClassNotFoundException
    {
        File f = new File(path);
            if (!f.exists())
            {
                return null;
            }
        List<BaseMessage> loglist =  GetBM_List(path);
            if (loglist != null)
            {
                for (BaseMessage bm : loglist)
                {
                    Itog tmp = (Itog) bm;
                    if (tmp.user_email.equalsIgnoreCase(mail))//mail is used
                    {
                        return tmp;
                    }
                }
            }
        return null;
    }
    static public Itog Get_Itog(String message, List<BaseMessage> loglist)
    {
        for (BaseMessage bm : loglist)
        {
            if (((Itog) bm).user_email.equalsIgnoreCase(message))
            {
                return (Itog) bm;
            }
        }
        return null;
    }
    static public List<BaseMessage> Set_Itog(Itog itg, List<BaseMessage> loglist)
    {
        for (int i=0;i<loglist.size();i++)
        {
            if (((Itog) loglist.get(i)).user_email.equalsIgnoreCase(itg.user_email))
            {
                loglist.set(i,(BaseMessage)itg);
                return loglist;
            }
        }
        return null;
    }
    static public List<BaseMessage> Set_DFR(DataForRecord dfr, List<BaseMessage> loglist)
    {
        for (int i=0;i<loglist.size();i++)
        {
            if (loglist.get(i).getClass() == DataForRecord.class && ((DataForRecord) loglist.get(i)).getTypeEvent() == dfr.getTypeEvent())
            {
                loglist.set(i,(BaseMessage)dfr);
                return loglist;
            }
        }
        return null;
    }
    
    static public ping GetStatusSession(String path, String mail) throws IOException, FileNotFoundException, ClassNotFoundException
    {
        Itog itg = Find_Itog(path, mail);
        if(itg!=null)
        {
            if(itg.SS==Itog.StatusSession.not_open)
            {
                return new ping("SessionNotOpen"); 
            }
            if(itg.SS==Itog.StatusSession.open)
            {
                return new ping("SessionAlreadyOpen"); 
            }
            if(itg.SS==Itog.StatusSession.close)
            {
                return new ping("SessionAlreadyClose"); 
            }
        }
        return new ping("ErrorStatusSession"); 
    }
    
    static public BaseMessage Find_BM(BaseMessage newbm, String path) throws IOException, FileNotFoundException, ClassNotFoundException
    {
        File f = new File(path);
            if (!f.exists())
            {
                return null;
            }
        List<BaseMessage> loglist =  GetBM_List(path);
            if (loglist != null)
            {
                for (BaseMessage bm : loglist)
                {
                    if (bm.equals(newbm))//is used
                    {
                        return bm;
                    }
                }
            }
        return null;
    }
    
    static public void AddMessage(BaseMessage bm, String path) throws IOException, FileNotFoundException, ClassNotFoundException
    {
        File f = new File(path);
            if (!f.exists())
            {
                f.createNewFile();
            }
        List<BaseMessage> loglist =  GetBM_List(path);
                if (loglist != null)
                {
                    loglist.add(bm);
                }
                else
                {
                    loglist = new ArrayList();
                    loglist.add(bm);
                }
            AddMessage(loglist, path);
    }
    static public void AddMessage(List<BaseMessage> loglist, String path) throws IOException, FileNotFoundException, ClassNotFoundException
    {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos); // Файл нужно перезаписывать новым листом
                oos.writeObject(loglist);
            oos.close();
            fos.close();
    }
    static private Itog Itog_DFR(Itog newitog,DataForRecord dfr)
    {
        int i=0;
        if(dfr.getTypeEvent()==DataForRecord.TypeEvent.open)
        {
            i=0;
        }
        if(dfr.getTypeEvent()==DataForRecord.TypeEvent.drug)
        {
            i=1;
        }
        if(dfr.getTypeEvent()==DataForRecord.TypeEvent.steal)
        {
            i=2;
        }
        if(dfr.getTypeEvent()==DataForRecord.TypeEvent.close)
        {
            i=3;
        }
            newitog.amountbag[i]=dfr.getsumm(0);
            newitog.weightall[i]=dfr.getsumm(1);
            newitog.amount_s += (int)(dfr.matrix[3][0])*((i/2)*(-2)+1);
            newitog.amount_k += (int)(dfr.getsumm(4))*((i/2)*(-2)+1);
            newitog.amount_t += (int)(dfr.matrix[3][1])*((i/2)*(-2)+1);
        return newitog;
    }
    static private Itog Itog_mulct(Itog newitog,user myuser)
    {
            double mulct=(newitog.weightall[0]+newitog.amountbag[0] * 2400 +
                            newitog.weightall[1]+newitog.amountbag[1] * 2400 -
                            newitog.weightall[2]-newitog.amountbag[2] * 2400 -
                            newitog.weightall[3]-newitog.amountbag[3] * 2400) -
                    (newitog.amount_s*myuser.weight_s + newitog.amount_k*myuser.weight_k + 
                    newitog.amount_t*myuser.weight_t);
            if (mulct > 200)
            {
                newitog.add_mulct(new BigDecimal(mulct*1.66).setScale(2, RoundingMode.UP).doubleValue(), "Перевес");
            }
            if (newitog.date_open.getHours() > myuser.NRD.getHours())
            {
                mulct = (newitog.date_open.getHours() - myuser.NRD.getHours()) * 60 * 15;
                if (mulct < 0)
                {
                    mulct = 0;
                }
                newitog.add_mulct(mulct + newitog.date_open.getMinutes() * 15, "Опоздание");
            }   
        return newitog;
    }
    static public Itog Calculate_Itog(Itog newitog,user myuser, String path) throws IOException, FileNotFoundException, ClassNotFoundException
    {
            DataForRecord dfr;
            newitog.day_otw = API.weektoString(newitog.date_open.getDay());
            dfr = Get_DFR(DataForRecord.TypeEvent.open,path);
            if(dfr!=null)
            {
                Itog_DFR(newitog,dfr);
                ping p = Get_ping("open", path);
                if(p != null)
                {
                    newitog.date_open = p.GetDate();
                }
                newitog.SS=Itog.StatusSession.open;
            }
            dfr = Get_DFR(DataForRecord.TypeEvent.drug,path);
            if(dfr!=null)
            {
                Itog_DFR(newitog,dfr);
            }
            dfr = Get_DFR(DataForRecord.TypeEvent.steal,path);
            if(dfr!=null)
            {
                Itog_DFR(newitog,dfr);
            }
            dfr = Get_DFR(DataForRecord.TypeEvent.close,path);
            if(dfr!=null)
            {
                Itog_DFR(newitog,dfr);
                Itog_mulct(newitog,myuser);
                newitog.date_close=dfr.GetDate();
                newitog.salary = (double)((newitog.date_close.getHours()-newitog.date_open.getHours()) * 60 +
                        (newitog.date_close.getMinutes()-newitog.date_open.getMinutes())) / 60 * myuser.salary;
                newitog.salary = new BigDecimal(newitog.salary).setScale(2, RoundingMode.UP).doubleValue();
                newitog.SS=Itog.StatusSession.close;
            }
            DataCass[] dc = Get_summ_DC(path);
                System.out.println("dc");
            if(dc!=null)
            {
                for (DataCass dc1 : dc)
                {
                    newitog.add_DC(dc1);
                }
            }
        return newitog;
    }
    static public Itog Calculate_Itog(Itog newitog,user myuser, List<BaseMessage> loglist) throws IOException, FileNotFoundException, ClassNotFoundException
    {
            DataForRecord dfr;
            newitog.day_otw = API.weektoString(newitog.date_open.getDay());
            dfr = Get_DFR(DataForRecord.TypeEvent.open,loglist);
            if(dfr!=null)
            {
                Itog_DFR(newitog,dfr);
                ping p = Get_ping("open", loglist);
                if(p != null)
                {
                    newitog.date_open = p.GetDate();
                }
                newitog.SS=Itog.StatusSession.open;
            }
            dfr = Get_DFR(DataForRecord.TypeEvent.drug,loglist);
            if(dfr!=null)
            {
                Itog_DFR(newitog,dfr);
            }
            dfr = Get_DFR(DataForRecord.TypeEvent.steal,loglist);
            if(dfr!=null)
            {
                Itog_DFR(newitog,dfr);
            }
            dfr = Get_DFR(DataForRecord.TypeEvent.close,loglist);
            if(dfr!=null)
            {
                Itog_DFR(newitog,dfr);
                Itog_mulct(newitog,myuser);
                newitog.date_close=dfr.GetDate();
                newitog.salary = (double)((newitog.date_close.getHours()-newitog.date_open.getHours()) * 60 +
                        (newitog.date_close.getMinutes()-newitog.date_open.getMinutes())) / 60 * myuser.salary;
                newitog.salary = new BigDecimal(newitog.salary).setScale(2, RoundingMode.UP).doubleValue();
                newitog.SS=Itog.StatusSession.close;
            }
            DataCass[] dc = Get_summ_DC(loglist);
                System.out.println("dc");
            if(dc!=null)
            {
                for (DataCass dc1 : dc)
                {
                    newitog.add_DC(dc1);
                }
            }
        return newitog;
    }
    
    static public List<BaseMessage> GetBM_List(String file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream read = new ObjectInputStream(fis);
                List<BaseMessage> loglist = (List) read.readObject();
            fis.close();
            read.close();
        return loglist;
    }
    static public ping Get_ping(String message, String file) throws IOException, FileNotFoundException, ClassNotFoundException
    {
        ping tempping = null;
        List<BaseMessage> loglist = GetBM_List(file);
            if (loglist != null)
            {
                tempping = Get_ping(message,loglist);
            }
        return tempping;
    }
    static public ping Get_ping(String message, List<BaseMessage> loglist)
    {
        for (BaseMessage bm : loglist)
        {
            if (bm.getClass() == ping.class && ((ping) bm).GetPing().equals(message))
            {
                return (ping) bm;
            }
        }
        return null;
    }
    static public DataCass[] Get_summ_DC(String file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        DataCass tempdc[] = null;
        List<BaseMessage> loglist = GetBM_List(file);
        return Get_summ_DC(loglist);
    }
    static public DataCass[] Get_summ_DC(List<BaseMessage> loglist) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        DataCass tempdc[] = null;
                if (loglist != null)
                {
                    tempdc = Get_DC(loglist);
                }
        return tempdc;
    }
    static private DataCass[] Get_DC(List<BaseMessage> loglist) throws FileNotFoundException, IOException
    {
        List<DataCass> arraydfr = new ArrayList();
        for (BaseMessage bm : loglist)
        {
            if (bm.getClass() == DataCass.class)
            {
                arraydfr.add((DataCass) bm);
            }
        }
        DataCass[] dc = new DataCass[arraydfr.size()];
        for(int i=0;i<dc.length;i++)
        {
            dc[i]=arraydfr.get(i);
        }
        return dc;
    }
    static public DataCass Get_DC(DataCass.TypeEvent TE, String file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        DataCass tempdc = null;
        List<BaseMessage> loglist = GetBM_List(file);
                if (loglist != null)
                {
                    tempdc = Get_DC(TE,loglist);
                }
        return tempdc;
    }
    static public DataCass Get_DC(DataCass.TypeEvent typeEvent,  List<BaseMessage> loglist) throws FileNotFoundException, IOException
    {
        for (BaseMessage bm : loglist)
        {
            if (bm.getClass() == DataCass.class && ((DataCass) bm).getTypeEvent() == typeEvent)
            {
                return (DataCass) bm;
            }
        }
        return null;
    }
    
    static public DataForRecord Get_DFR(DataForRecord.TypeEvent TE, String file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        DataForRecord tempdfr = null;
        List<BaseMessage> loglist = GetBM_List(file);
                if (loglist != null)
                {
                    if (TE == DataForRecord.TypeEvent.open || TE == DataForRecord.TypeEvent.close)
                    {
                        tempdfr = Get_DFR(TE,loglist);
                    }
                    else
                    {
                        tempdfr = Get_summ_DFR(TE,loglist);
                    }
                }
        return tempdfr;
    }
    
    static public DataForRecord Get_summ_DFR(DataForRecord.TypeEvent typeEvent, List<BaseMessage> loglist)
    {
        DataForRecord dfr = null;
        for (BaseMessage bm : loglist)
        {
            if (bm.getClass() == DataForRecord.class && ((DataForRecord) bm).getTypeEvent() == typeEvent)
            {
                if(dfr == null)
                {
                    dfr = (DataForRecord) bm;
                }
                else
                {
                    dfr.addData((DataForRecord) bm, true);
                }
            }
        }
        return dfr;
    }
    static public DataForRecord Get_DFR(DataForRecord.TypeEvent typeEvent, List<BaseMessage> loglist)
    {
        for (BaseMessage bm : loglist)
        {
            if (bm.getClass() == DataForRecord.class && ((DataForRecord) bm).getTypeEvent() == typeEvent)
            {
                return (DataForRecord) bm;
            }
        }
        return null;
    }
    
    static public String weektoString(int w)
    {
        switch (w)
        {
            case 1:
                return "Понедельник";
            case 2:
                return "Вторник";
            case 3:
                return "Среда";
            case 4:
                return "Четверг";
            case 5:
                return "Пятница";
            case 6:
                return "Суббота";
            case 0:
                return "Воскресенье";
        }
        return "fail";
    }
}