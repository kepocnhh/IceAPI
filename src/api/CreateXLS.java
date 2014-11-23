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
    static public void SetProp(String[] cells)
    {
        line_cs = new CellStyle[cells.length];
        for(int i=0; i<cells.length;i++)
        {
            
        }
    }
    static private CellStyle[] line_cs;
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
    static private Row create_row(Itog newitog, user newuser, Row r,CellStyle cs)
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
            //str +=date_to_rus_string(newitog.date_open)+"\t";//дата
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
                Font f = r.getSheet().getWorkbook().createFont();
                f.setColor(IndexedColors.BLACK.getIndex());
                //cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
            CellStyle new_cs = r.getSheet().getWorkbook().createCellStyle();
            new_cs.cloneStyleFrom(cs);
                new_cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            if( i==0)
            {
                f.setColor(IndexedColors.WHITE.getIndex());
                new_cs.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
            }
            if(i==1 || i==2 || //кепки
                    i==4 || i==5 || //стаканы
                    i==7 || i==8||//термосы
                    i==12 || i==13||//бонус
                    i==14 || i==15||//вес кепок
                    i==16 || i==17||//вес стаканов
                    i==18 || i==19||//вес термосов
                    i==24 || i==25//ставка
                    )
            {
                if(i==1 || i==4 || i==7|| i==12|| i==14|| i==16|| i==18|| i==24)
                {
                    f.setColor(IndexedColors.LIGHT_BLUE.getIndex());
                }
                new_cs.setLeftBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
            }
            if( i==10 || i==11||
                    i==20 || i==21)
            {
                if(i==10||i==20)
                {
                    f.setColor(IndexedColors.GREEN.getIndex());
                }
                new_cs.setLeftBorderColor(IndexedColors.GREEN.getIndex());
            }
            if( i==26 || i==27)
            {
                if(i==26)
                {
                    f.setColor(IndexedColors.RED.getIndex());
                }
                new_cs.setLeftBorderColor(IndexedColors.RED.getIndex());
            }
            new_cs.setFont(f);
            c = r.createCell(i);
            c.setCellValue(cell[i]);
            c.setCellStyle(new_cs);
        }
        return r;
    }
    static public CellStyle cell_borders_back(CellStyle c, short c_bo, short c_ba, short s_bo)
    {
                c.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                c.setFillBackgroundColor(c_ba);
                c.setFillPattern(IndexedColors.WHITE.getIndex());
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
        Row r_ttl = null;
        r_ttl = s.createRow(0);
        Cell c_ttl = null;
            CellStyle ttl_cs = wb.createCellStyle();
            ttl_cs = cell_borders_back(ttl_cs, IndexedColors.BLACK.getIndex(), IndexedColors.GOLD.getIndex(), CellStyle.BORDER_MEDIUM);
        for (int i = 0; i < cell.length; i++)
        {
            c_ttl = r_ttl.createCell(i);
            c_ttl.setCellValue(cell[i]);
            c_ttl.setCellStyle(ttl_cs);
        }
                int day = 0;
                int mnth = 0;
                int numrow = 1;
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
                CellStyle rs = wb.createCellStyle();
                Font f_m = wb.createFont();
                f_m.setColor(IndexedColors.WHITE.getIndex());
                f_m.setFontHeight((short)333);
                rs.setFont(f_m);
                rs.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                rs.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                rs.setFillPattern(IndexedColors.WHITE.getIndex());
                Row row_m = s.createRow(numrow);
                numrow++;
                Cell c_m = row_m.createCell(0);
                c_m.setCellValue(month_to_rus_string(newitog[i].date_close)+" "+(newitog[i].date_close.getYear()+1900));
                c_m.setCellStyle(rs);
                row_m.setRowStyle(rs);
                row_m.setHeight((short)444);
            }
            Row row = s.createRow(numrow);
            numrow++;
            CellStyle cs = wb.createCellStyle();
            cs = cell_borders_back(cs, IndexedColors.BLACK.getIndex(), IndexedColors.WHITE.getIndex(), CellStyle.BORDER_MEDIUM);
            if(day!=newitog[i].date_close.getDate())
            {
                cs.setTopBorderColor(IndexedColors.LIGHT_BLUE.getIndex());
                cs.setBorderTop(CellStyle.BORDER_THIN);
            }
            day = newitog[i].date_close.getDate();
            mnth = newitog[i].date_close.getMonth();
            create_row(newitog[i], newuser, row, cs);
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
