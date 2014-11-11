package api;

import ice.DataCass;
import ice.Itog;
import ice.user;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class CreateXLS
{
    static private String date_to_rus_string(Date d)
    {
        String str= "";
            str+=d.getDate();
            str+=" ";
        if(d.getMonth()==0)
        {
            str+="Января";
        }
        else if(d.getMonth()==1)
        {
            str+="Февраля";
        }
        else if(d.getMonth()==2)
        {
            str+="Марта";
        }
        else if(d.getMonth()==3)
        {
            str+="Апреля";
        }
        else if(d.getMonth()==4)
        {
            str+="Мая";
        }
        else if(d.getMonth()==5)
        {
            str+="Июня";
        }
        else if(d.getMonth()==6)
        {
            str+="Июля";
        }
        else if(d.getMonth()==7)
        {
            str+="Августа";
        }
        else if(d.getMonth()==8)
        {
            str+="Сентября";
        }
        else if(d.getMonth()==9)
        {
            str+="Октября";
        }
        else if(d.getMonth()==10)
        {
            str+="Ноября";
        }
        else if(d.getMonth()==11)
        {
            str+="Декабря";
        }
            str+=" ";
            str+=(d.getYear()+1900);
            str+=" ";
            str+="года";
        return str;
    }
    static private Row create_row(Itog newitog, user newuser, Row r)
    {
        Cell c;
        String ink = "";
        String pre = "";
        String pro = "";
        String mulct = "";
        for(int i=0;i<newitog.get_DC().length;i++)
        {
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.inkasator)
            {
                ink+=" "+newitog.get_DC()[i].getCash();
            }
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.cass)
            {
                pre+=" "+newitog.get_DC()[i].getCash();
            }
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.promoter)
            {
                pro+=" "+newitog.get_DC()[i].getCash();
            }
        }
        ink+="\t";
        pre+="\t";
        for(int i=0;i<newitog.get_DC().length;i++)
        {
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.inkasator)
            {
                ink+=" "+newitog.get_DC()[i].getFam();
            }
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.cass)
            {
                pre+=" "+newitog.get_DC()[i].getFam();
            }
        }
            //System.out.println("newitog.get_mulct().length "+newitog.get_mulct().length);
        for(int i=0;i<newitog.get_mulct().length;i++)
        {
            mulct+="["+API.my_round(newitog.get_mulct()[i],2)+"] ";
        }
        mulct+="\t";
        for(int i=0;i<newitog.get_mulct().length;i++)
        {
            mulct+="["+newitog.get_mulct_str()[i]+"] ";
        }
            String str = "";
            str +=date_to_rus_string(newitog.date_open)+"\t";//дата
            str +=newitog.amount_k+"\t";//количество проданных кепок
            str +=newuser.price_k+"\t";//цена кепок
               str += (newitog.amount_k*newuser.price_k)+"\t";//выручка за кепки
                str +=newitog.amount_s+"\t";
                str +=newuser.price_s+"\t";
                str +=(newitog.amount_s*newuser.price_s)+"\t";
                str +=newitog.amount_t+"\t";
                str +=newuser.price_t+"\t";
                str +=(newitog.amount_t*newuser.price_t)+"\t";
                str +=((newitog.amount_k*newuser.price_k)+(newitog.amount_s*newuser.price_s)+(newitog.amount_t*newuser.price_t))+"\t";
                str +=newuser.GetSurname()+"\t";
                str +=newuser.bonus+"%"+"\t";
                str +=((newitog.amount_k*newuser.price_k)/100*newuser.bonus)+"\t";
                str +=newuser.weight_k+"\t";
                str +=(newitog.amount_k*newuser.weight_k)+"\t";
                str +=newuser.weight_s+"\t";
                str +=(newitog.amount_s*newuser.weight_s)+"\t";
                str +=newuser.weight_t+"\t";
                str +=(newitog.amount_t*newuser.weight_t)+"\t";
                str +=((newitog.amount_k*newuser.weight_k)+(newitog.amount_s*newuser.weight_s)+(newitog.amount_t*newuser.weight_t))+"\t";//итого вес
            String s;
            s = ""+newitog.date_open.getMinutes();
        if (s.length() == 1)
        {
            s = "0" + s;
        }
                str +=newitog.date_open.getHours() + ":" + s+"\t";
            s = ""+newitog.date_close.getMinutes();
        if (s.length() == 1)
        {
            s = "0" + s;
        }
                str +=newitog.date_close.getHours() + ":" + s+"\t";
                str += API.my_round((double)((newitog.date_close.getHours()-newitog.date_open.getHours()) * 60 +(newitog.date_close.getMinutes()-newitog.date_open.getMinutes())) / 60, 2)+"\t";
                str +=newuser.salary+"\t";
                str +=newitog.salary+"\t";
                str += API.my_round((newitog.salary +
                                        ((double)newitog.amount_k*newuser.price_k / 100) * newuser.bonus),2)+"\t";//итого зп
                str +=ink+"\t";
                str +=pre+"\t";
                str +=pro+"\t";
                str +=mulct+"\t";
                str +=newitog.day_otw+"\t";
                str += API.my_round((newitog.salary +
                                        ((double)newitog.amount_k*newuser.price_k / 100) * newuser.bonus -
                                        newitog.get_summ_mulct()),2)+"\t";//на руки
        String[] cell=(str).split("\t");
        for (int i = 0; i < cell.length; i++)
        {
            c = r.createCell(i);
            c.setCellValue(cell[i]);
        }
        return r;
    }
    static public void _CreateXLS(Itog newitog[],String name,List<user> ul) 
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        Workbook wb = new HSSFWorkbook();
        Sheet s = wb.createSheet();
        Row r = null;
        Cell c = null;
        String[] cell;
        CellStyle cs = wb.createCellStyle();
        Font f = wb.createFont();
        f.setFontHeightInPoints((short) 12);
        f.setColor(Short.MIN_VALUE);
//        cs.setFont(f);
//        cs.setDataFormat(wb.createDataFormat().getFormat("#,##0.0"));
//                cs.setTopBorderColor((short)0);
//                cs.setBorderTop((short)2);
        wb.setSheetName(0, "ICENGO" );
        //title/////////////////////////////////////////////////////////////
        r = s.createRow(0);
        //
        cell=(
                "Дата"+"\t"+
                "Кепки"+"\t"+
                "Цена"+"\t"+
                "Выручка Кепки"+"\t"+
                "Стаканы"+"\t"+
                "Цена"+"\t"+
                "Выручка Стаканы"+"\t"+
                "Термосы"+"\t"+
                "Цена"+"\t"+
                "Выручка Термосы"+"\t"+
                "Выручка"+"\t"+
                "Сотрудник"+"\t"+
                "Бонус"+"\t"+
                "Сумма Бонуса"+"\t"+
                "Вес"+"\t"+
                "Вес кепок"+"\t"+
                "Вес"+"\t"+
                "Вес стаканов"+"\t"+
                "Вес"+"\t"+
                "Вес термосов"+"\t"+
                "Итого ВЕС"+"\t"+
                "Начало смены"+"\t"+
                "Конец смены"+"\t"+
                "Часы"+"\t"+
                "Ставка"+"\t"+
                "Оклад"+"\t"+
                "ИТОГО ЗП"+"\t"+
                "Инкассация"+"\t"+
                "Лицо"+"\t"+
                "Аванс"+"\t"+
                "Сотрудник"+"\t"+
                "Промоутер"+"\t"+
                "Штрафы"+"\t"+
                "Описание"+"\t"+
                "День недели"+"\t"+
                "на руки"
                ).split("\t");
        for (int i = 0; i < cell.length; i++)
        {
            c = r.createCell(i);
            c.setCellValue(cell[i]);
        }
                int day = 0;
        for(int i = 0; i < newitog.length; i++)
        {
                    user newuser = null;
            for (user ul1 : ul) 
            {
                if (newitog[i].user_email.equals(ul1.GetMail())) 
                {
                    newuser = ul1;
                    break;
                }
            }
            if(newuser == null)
            {
                System.out.println("user null\n"+newitog[i].user_email);
                continue;
            }
                System.out.println("--> "+day);
            Row row = s.createRow(i+1);
            create_row(newitog[i], newuser, row);
        }
        FileOutputStream out = new FileOutputStream(name+".xls");
            wb.write(out);
            out.close();
    }
}
