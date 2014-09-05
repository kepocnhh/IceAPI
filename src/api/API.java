package api;

import ice.BaseMessage;
import ice.DataCass;
import ice.DataForRecord;
import ice.Itog;
import ice.ping;
import ice.user;
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
    //Файл нужно перезаписывать новым листом/////////////////////////////////////////////
    static public void AddMessage(List<BaseMessage> loglist, String path) throws IOException, FileNotFoundException, ClassNotFoundException
    {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(loglist);
            oos.close();
            fos.close();
    }
    //BaseMessage/////////////////////////////////////////////////////////////////////////
    static public List<BaseMessage> Get_BM_List(String file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream read = new ObjectInputStream(fis);
                List<BaseMessage> loglist = (List) read.readObject();
            fis.close();
            read.close();
        return loglist;
    }
    static public BaseMessage Get_BM(BaseMessage newbm, List<BaseMessage> loglist)
    {
        for (BaseMessage bm : loglist)
        {
            if (bm.equals(newbm))//is used
            {
                return bm;
            }
        }
        return null;
    }
    //ping/////////////////////////////////////////////////////////////////////////////////
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
    //user/////////////////////////////////////////////////////////////////////////////////
    static public user Get_user(String email, List<BaseMessage> loglist) throws IOException, ClassNotFoundException
    {
        for (BaseMessage bm : loglist)
        {
            if (bm.getClass() == user.class && ((user) bm).GetMail().equalsIgnoreCase(email))
            {
                return (user) bm;
            }
        }
        return null;
    }
    //Itog/////////////////////////////////////////////////////////////////////////////////
    static public Itog Get_Itog(String message, List<BaseMessage> loglist)
    {
        for (BaseMessage bm : loglist)
        {
            if (bm.getClass() == Itog.class && ((Itog) bm).user_email.equalsIgnoreCase(message))
            {
                return (Itog) bm;
            }
        }
        return null;
    }
    //DataCass/////////////////////////////////////////////////////////////////////////////
    static public List<DataCass> Get_DC_List(List<BaseMessage> loglist) throws FileNotFoundException, IOException
    {
        List<DataCass> arraydfr = new ArrayList();
        for (BaseMessage bm : loglist)
        {
            if (bm.getClass() == DataCass.class)
            {
                arraydfr.add((DataCass) bm);
            }
        }
        return arraydfr;
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
    //DataForRecord///////////////////////////////////////////////////////////////////////
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
    static public DataForRecord Get_DFR_summ(DataForRecord.TypeEvent typeEvent, List<BaseMessage> loglist)
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
    //DataForRecord///////////////////////////////////////////////////////////////////////
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
    //Itog/////////////////////////////////////////////////////////////////////////////////
    static public List<BaseMessage> Set_Itog(Itog itg, List<BaseMessage> loglist)
    {
        for (int i=0;i<loglist.size();i++)
        {
            if (loglist.get(i).getClass() == Itog.class && ((Itog) loglist.get(i)).user_email.equalsIgnoreCase(itg.user_email))
            {
                loglist.set(i,(BaseMessage)itg);
                return loglist;
            }
        }
        return null;
    }
    //Calculate Itog////////////////////////////////////////////////////////////////////////
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
            List<DataCass> dc = Get_DC_List(loglist);
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
    //Дни недели
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