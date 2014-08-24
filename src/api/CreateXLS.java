package api;

import ice.DataCass;
import ice.Itog;
import ice.user;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
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
            System.out.println("newitog.get_DC() "+newitog.get_DC());
            System.out.println("newitog.get_DC().length "+newitog.get_DC().length);
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
        mulct+="\t";
        for(int i=0;i<newitog.get_mulct().length;i++)
        {
            mulct+=" "+newitog.get_mulct_str()[i];
        }
        String[] cell=(
                newitog.date_open+"\t"+
                newitog.amount_k+"\t"+
                newuser.price_k+"\t"+
                (newitog.amount_k*newuser.price_k)+"\t"+
                newitog.amount_s+"\t"+
                newuser.price_s+"\t"+
                (newitog.amount_s*newuser.price_s)+"\t"+
                newitog.amount_t+"\t"+
                newuser.price_t+"\t"+
                (newitog.amount_t*newuser.price_t)+"\t"+
                ((newitog.amount_k*newuser.price_k)+(newitog.amount_s*newuser.price_s)+(newitog.amount_t*newuser.price_t))+"\t"+
                newuser.GetSurname()+"\t"+
                newuser.bonus+"\t"+
                (newitog.amount_k*newuser.bonus)+"\t"+
                newuser.weight_k+"\t"+
                (newitog.amount_k*newuser.weight_k)+"\t"+
                newuser.weight_s+"\t"+
                (newitog.amount_s*newuser.weight_s)+"\t"+
                newuser.weight_t+"\t"+
                (newitog.amount_t*newuser.weight_t)+"\t"+
                ((newitog.amount_k*newuser.weight_k)+(newitog.amount_s*newuser.weight_s)+(newitog.amount_t*newuser.weight_t))+"\t"+
                newitog.date_open.getHours() + ":" + CreatePDF.minutes(""+newitog.date_open.getMinutes())+"\t"+
                newitog.date_close.getHours() + ":" + CreatePDF.minutes(""+newitog.date_close.getMinutes())+"\t"+
                (double)((newitog.date_close.getHours()-newitog.date_open.getHours()) * 60 +
                    (newitog.date_close.getMinutes()-newitog.date_open.getMinutes())) / 60+"\t"+
                newuser.salary+"\t"+
                newitog.salary+"\t"+
                (newitog.salary+newitog.amount_k*newuser.bonus)+"\t"+
                ink+"\t"+
                pre+"\t"+
                pro+"\t"+
                mulct+"\t"+
                newitog.day_otw+"\t"+
                ((newitog.salary+newitog.amount_k*newuser.bonus)-newitog.get_summ_mulct())
                ).split("\t");
        for (int i = 0; i < cell.length; i++)
        {
            c = r.createCell(i);
            c.setCellValue(cell[i]);
        }
        return r;
    }
    static public void _CreateXLS(
            Date d1, Date d2,
            String name) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        
    }
    static public void _CreateXLS(
            Itog newitog[],
            String name) throws FileNotFoundException, IOException, ClassNotFoundException
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
        for(int i = 0; i < newitog.length; i++)
        {
            create_row(newitog[i], API.Find_user(API.accpath, newitog[i].user_email), s.createRow(1));
        }
        FileOutputStream out = new FileOutputStream(name+".xls");
            wb.write(out);
            out.close();
    }
}
