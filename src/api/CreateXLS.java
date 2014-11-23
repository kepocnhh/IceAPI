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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class CreateXLS
{
    static private CellStyle[] line_cs;
    static private CellStyle[] new_day_cs;
    static private CellStyle month_cs;
    static private CellStyle ttl_cs;
    static public void SetProp(Workbook wb)
    {
        new_day_cs = new CellStyle[line_cs.length];
        for(int i=0; i<new_day_cs.length;i++)
        {
            new_day_cs[i] = wb.createCellStyle();
            new_day_cs[i].cloneStyleFrom(line_cs[i]);
            //new_day_cs[i].setBottomBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
            //new_day_cs[i].setBorderBottom(CellStyle.BORDER_MEDIUM);
            //new_day_cs[i].setBorderBottom(CellStyle.BORDER_DOUBLE);
            new_day_cs[i].setBorderTop(CellStyle.BORDER_MEDIUM);
        }
    }
    static public CellStyle SetProp(CellStyle cs, Font f, short col)
    {
                cs.setLeftBorderColor(col);
                cs.setBottomBorderColor(col);
                cs.setTopBorderColor(col);
                cs.setBorderLeft(CellStyle.BORDER_THIN);
                cs.setBorderBottom(CellStyle.BORDER_THIN);
                cs.setBorderTop(CellStyle.BORDER_THIN);
                cs.setFont(f);
                return cs;
    }
    static public void SetProp(String[] cells, CellStyle new_cs, Font f, Workbook wb)
    {
        line_cs = new CellStyle[cells.length];
        //
            ttl_cs = wb.createCellStyle();
            ttl_cs.cloneStyleFrom(new_cs);
            ttl_cs = cell_borders_back(ttl_cs, IndexedColors.BLACK.getIndex(), IndexedColors.GOLD.getIndex(), CellStyle.BORDER_THIN);
        //
            month_cs = wb.createCellStyle();
            month_cs.cloneStyleFrom(new_cs);
            Font f_m = wb.createFont();
            f_m.setColor(IndexedColors.WHITE.getIndex());
            f_m.setFontHeight((short)333);
            month_cs.setFont(f_m);
            month_cs.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            month_cs.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            month_cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
                month_cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
                month_cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
                month_cs.setLeftBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
                month_cs.setRightBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
                    month_cs.setBorderBottom(CellStyle.BORDER_THIN);
                    month_cs.setBorderTop(CellStyle.BORDER_MEDIUM);
                    //month_cs.setBorderTop(CellStyle.BORDER_THIN);
                    month_cs.setBorderLeft(CellStyle.BORDER_NONE);
                    //month_cs.setBorderRight(CellStyle.BORDER_NONE);
                    month_cs.setBorderRight(CellStyle.BORDER_THIN);
        //
            CellStyle date_cs = wb.createCellStyle();
            date_cs.cloneStyleFrom(new_cs);
            Font date_f = wb.createFont();
                date_f.setColor(IndexedColors.WHITE.getIndex());
                date_cs.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                date_cs.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
                date_cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
                date_cs.setFont(date_f);
        //
            CellStyle blue_cs = wb.createCellStyle();
            blue_cs.cloneStyleFrom(new_cs);
            Font blue_f = wb.createFont();
                blue_f.setColor(IndexedColors.LIGHT_BLUE.getIndex());
            blue_cs = SetProp(blue_cs, blue_f, IndexedColors.LIGHT_BLUE.getIndex());
            CellStyle postblue_cs = wb.createCellStyle();
            postblue_cs.cloneStyleFrom(new_cs);
                postblue_cs.setLeftBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
                postblue_cs.setBorderLeft(CellStyle.BORDER_THIN);
        //
            CellStyle green_cs = wb.createCellStyle();
            green_cs.cloneStyleFrom(new_cs);
            Font green_f = wb.createFont();
                green_f.setColor(IndexedColors.GREEN.getIndex());
            green_cs = SetProp(green_cs, green_f, IndexedColors.GREEN.getIndex());
            CellStyle postgreen_cs = wb.createCellStyle();
            postgreen_cs.cloneStyleFrom(new_cs);
                postgreen_cs.setLeftBorderColor(IndexedColors.GREEN.getIndex());
                postgreen_cs.setBorderLeft(CellStyle.BORDER_THIN);
        //
            CellStyle red_cs = wb.createCellStyle();
            red_cs.cloneStyleFrom(new_cs);
            Font red_f = wb.createFont();
                red_f.setColor(IndexedColors.RED.getIndex());
            red_cs = SetProp(red_cs, red_f, IndexedColors.RED.getIndex());
            CellStyle postred_cs = wb.createCellStyle();
            postred_cs.cloneStyleFrom(new_cs);
                postred_cs.setLeftBorderColor(IndexedColors.RED.getIndex());
                postred_cs.setBorderLeft(CellStyle.BORDER_THIN);
        //
        for(int i=0; i<cells.length;i++)
        {
            if( i==0)
            {
                line_cs[i] = date_cs;
                continue;
            }
            if(i==2 || //кепки
                    i==5 || //стаканы
                    i==8||//термосы
                    i==13||//бонус
                    i==15||//вес кепок
                    i==17||//вес стаканов
                    i==19||//вес термосов
                    i==25//ставка
                    )
            {
                line_cs[i] = postblue_cs;
                continue;
            }
            if(i==1 || i==4 || i==7|| i==12|| i==14|| i==16|| i==18|| i==24)
            {
                line_cs[i] = blue_cs;
                continue;
            }
            if(i==11 || i==21)
            {
                line_cs[i] = postgreen_cs;
                continue;
            }
            if(i==10||i==20)
            {
                line_cs[i] = green_cs;
                continue;
            }
            if(i==27)
            {
                line_cs[i] = postred_cs;
                continue;
            }
            if(i==26)
            {
                line_cs[i] = red_cs;
                continue;
            }
                line_cs[i] = new_cs;
        }
        SetProp(wb);
    }
    static private String month_to_rus_string(Date d)
    {
        String str = "";
        if(d.getMonth()==0)
        {
            str+="Январь";
        }
        else if(d.getMonth()==1)
        {
            str+="Февраль";
        }
        else if(d.getMonth()==2)
        {
            str+="Март";
        }
        else if(d.getMonth()==3)
        {
            str+="Апрель";
        }
        else if(d.getMonth()==4)
        {
            str+="Май";
        }
        else if(d.getMonth()==5)
        {
            str+="Июнь";
        }
        else if(d.getMonth()==6)
        {
            str+="Июль";
        }
        else if(d.getMonth()==7)
        {
            str+="Август";
        }
        else if(d.getMonth()==8)
        {
            str+="Сентябрь";
        }
        else if(d.getMonth()==9)
        {
            str+="Октябрь";
        }
        else if(d.getMonth()==10)
        {
            str+="Ноябрь";
        }
        else if(d.getMonth()==11)
        {
            str+="Декабрь";
        }
        return str;
    }
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
    static private Row create_row(Itog newitog, user newuser, Row r, boolean mf)
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
                ink+=" ["+newitog.get_DC()[i].getCash() + "]";
            }
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.cass)
            {
                pre+=" ["+newitog.get_DC()[i].getCash() + "]";
            }
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.promoter)
            {
                pro+=" ["+newitog.get_DC()[i].getCash() + "]";
            }
        }
        ink+="\t";
        pre+="\t";
        for(int i=0;i<newitog.get_DC().length;i++)
        {
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.inkasator)
            {
                ink+=" ["+newitog.get_DC()[i].getFam() + "]";
            }
            if(newitog.get_DC()[i].getTypeEvent()==DataCass.TypeEvent.cass)
            {
                pre+=" ["+newitog.get_DC()[i].getFam() + "]";
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
            str +=newitog.date_open.getDate()+"\t";//дата
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
            if(mf)
            {
                c.setCellStyle(new_day_cs[i]);
            }
            else
            {
                c.setCellStyle(line_cs[i]);
            }
        }
        return r;
    }
    static public CellStyle cell_borders_back(CellStyle c, short c_bo, short c_ba, short s_bo)
    {
                c.setFillForegroundColor(c_ba);
                c.setFillBackgroundColor(c_ba);
                c.setFillPattern(CellStyle.SOLID_FOREGROUND);
                c.setTopBorderColor(c_bo);
                c.setBottomBorderColor(c_bo);
                c.setLeftBorderColor(c_bo);
                c.setRightBorderColor(c_bo);
                c.setBorderTop(s_bo);
                c.setBorderBottom(s_bo);
                c.setBorderLeft(s_bo);
                c.setBorderRight(s_bo);
                return c;
    }
    static public void _CreateXLS(Itog newitog[],String name,List<user> ul) 
            throws FileNotFoundException, IOException, ClassNotFoundException
    {
        Workbook wb = new HSSFWorkbook();
        Sheet s = wb.createSheet();
        wb.setSheetName(0, "ICENGO" );
        //title/////////////////////////////////////////////////////////////
        //
        String[] cell;
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
        CellStyle black_cs = wb.createCellStyle();
        black_cs = cell_borders_back(black_cs, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), CellStyle.BORDER_THIN);
        Font black_f = wb.createFont();
        SetProp(cell,black_cs,black_f, wb);
            System.out.println("CreateXLS.SetProp good");
        Row r_ttl = null;
        r_ttl = s.createRow(0);
        Cell c_ttl = null;
        for (int i = 0; i < cell.length; i++)
        {
            c_ttl = r_ttl.createCell(i);
            c_ttl.setCellValue(cell[i]);
            c_ttl.setCellStyle(ttl_cs);
        }
                int day = 0;
                int mnth = 0;
                int numrow = 1;
            boolean monthflag = false;
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
            if(mnth != newitog[i].date_close.getMonth())
            {
                Row row_m = s.createRow(numrow);
                numrow++;
                Cell c_m = row_m.createCell(0);
                c_m.setCellValue(month_to_rus_string(newitog[i].date_close)+" "+(newitog[i].date_close.getYear()+1900));
                c_m.setCellStyle(month_cs);
                row_m.setRowStyle(month_cs);
                row_m.setHeight((short)444);
            }
            Row row = s.createRow(numrow);
            numrow++;
            if(day!=newitog[i].date_close.getDate())
            {
                monthflag = true;
            }
            else
            {
                monthflag = false;
            }
            day = newitog[i].date_close.getDate();
            mnth = newitog[i].date_close.getMonth();
            create_row(newitog[i], newuser, row, monthflag);
        }
                Row row_m = s.createRow(numrow);
                numrow++;
                Cell c_m = row_m.createCell(0);
                c_m.setCellValue("");
                c_m.setCellStyle(month_cs);
                row_m.setRowStyle(month_cs);
                row_m.setHeight((short)444);
        for (int i = 0; i<cell.length; i++)
        {
            s.autoSizeColumn(i);
        }
        FileOutputStream out = new FileOutputStream(name+".xls");
            wb.write(out);
            out.close();
    }
    static public void _CreateXLS_graphics(List<Itog> il,String name,List<user> ul)
    {
        Workbook wb = new HSSFWorkbook();
        Sheet s = wb.createSheet();
        Row r = null;
        Cell c = null;
        String[] cell;
        cell =(
                "Красные"+"\t" +
                "Синие"
                ).split("\t");
        wb.setSheetName(0, "ICENGO" );
        //title/////////////////////////////////////////////////////////////
        r = s.createRow(0);
        for (int i = 0; i < cell.length; i++)
        {
            c = r.createCell(i);
            c.setCellValue(cell[i]);
        }
    }
}
