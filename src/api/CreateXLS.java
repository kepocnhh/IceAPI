package api;

import ice.DataCass;
import ice.Itog;
import ice.user;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    static private Row create_row(Itog newitog, user newuser,Row r)
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
            System.out.println("newitog.get_mulct().length "+newitog.get_mulct().length);
        for(int i=0;i<newitog.get_mulct().length;i++)
        {
            mulct+=" "+newitog.get_mulct()[i];
        }
            System.out.println("get_mulct");
        mulct+="\t";
        for(int i=0;i<newitog.get_mulct().length;i++)
        {
            mulct+=" "+newitog.get_mulct_str()[i];
        }
            System.out.println("get_mulct_str");
            String str = "";
            str +=newitog.date_open+"\t";
            str +=newitog.amount_k+"\t";
            str +=newuser.price_k+"\t";
               str += (newitog.amount_k*newuser.price_k)+"\t";
                str +=newitog.amount_s+"\t";
                str +=newuser.price_s+"\t";
                str +=(newitog.amount_s*newuser.price_s)+"\t";
                str +=newitog.amount_t+"\t";
                str +=newuser.price_t+"\t";
                System.out.println("price");
                str +=(newitog.amount_t*newuser.price_t)+"\t";
                str +=((newitog.amount_k*newuser.price_k)+(newitog.amount_s*newuser.price_s)+(newitog.amount_t*newuser.price_t))+"\t";
                str +=newuser.GetSurname()+"\t";
                System.out.println("Surname = "+newuser.GetSurname());
                str +=newuser.bonus+"\t";
                str +=(newitog.amount_k*newuser.bonus)+"\t";
                str +=newuser.weight_k+"\t";
                str +=(newitog.amount_k*newuser.weight_k)+"\t";
                str +=newuser.weight_s+"\t";
                str +=(newitog.amount_s*newuser.weight_s)+"\t";
                str +=newuser.weight_t+"\t";
                str +=(newitog.amount_t*newuser.weight_t)+"\t";
                str +=((newitog.amount_k*newuser.weight_k)+(newitog.amount_s*newuser.weight_s)+(newitog.amount_t*newuser.weight_t))+"\t";
            System.out.println("amount");
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
            System.out.println("Hours");
                str +=(double)((newitog.date_close.getHours()-newitog.date_open.getHours()) * 60 +(newitog.date_close.getMinutes()-newitog.date_open.getMinutes())) / 60+"\t";
                str +=newuser.salary+"\t";
                str +=newitog.salary+"\t";
                str +=(newitog.salary+newitog.amount_k*newuser.bonus)+"\t";
            System.out.println("dc");
                str +=ink+"\t";
                str +=pre+"\t";
                str +=pro+"\t";
                str +=mulct+"\t";
                str +=newitog.day_otw+"\t";
                str +=((newitog.salary+newitog.amount_k*newuser.bonus)-newitog.get_summ_mulct());
        String[] cell=(str).split("\t");
            System.out.println("split");
            r.setHeightInPoints(111);
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
        cs.setFont(f);
        cs.setDataFormat(wb.createDataFormat().getFormat("#,##0.0"));
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
            System.out.println("create_row");
                System.out.println("\n"+newitog[0].user_email);
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
            create_row(newitog[i], newuser, s.createRow(1));
        }
        FileOutputStream out = new FileOutputStream(name+".xls");
            wb.write(out);
            out.close();
    }
}
