package api;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ice.DataCass;
import ice.DataForRecord;
import ice.Itog;
import ice.Strings;
import ice.user;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class CreatePDF
{
    private static Document document;
    private static Font font;
    private static Font fonttitle;
    
    public static void SetProp(String fonts) throws DocumentException, IOException
    {
        setfont(fonts);
    }
    public static void _CreatePDF(Strings strlist,
            user newuser,
            DataForRecord dfropen,
            DataForRecord dfrdrug,
            DataForRecord dfrsteal,
            DataForRecord dfrclose,
            Itog newitog,
            String name) throws DocumentException, FileNotFoundException
    {
        createpdf(name);
        int wts = 88;
        int wtm = 99;
        int wtl = 111;
        int BW = 0;
        PdfPTable nestedTable = new PdfPTable(3);
        nestedTable.setWidthPercentage(wtm);
        PdfPCell cll;
        //title///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("01"));
        cll = create_title_table(newitog.nameshop,(
                                        strlist.PDFheader.get(0)+"\n"+
                                        newuser.GetSurname() + " " + newuser.GetName() + " " + newuser.GetPatronymic()+"\n"+
                                        "Точка"+"\n"+
                                        newitog.nameshop
                                        ).split("\n"),
                                        cll, BW, wtm);
        nestedTable.addCell(cll);//01///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("02"));
        cll = create_title_table(dateonly(new Date()),(
                                        strlist.PDFheader.get(1)+"\n"+
                                        dfrclose.GetX() + " " + dfrclose.GetY()+"\n"+
                                        strlist.PDFheader.get(2)+"\n"+
                                        datetostring(newitog.date_open)
                                        ).split("\n"),
                                        cll, BW, wtm);
        nestedTable.addCell(cll);//02///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("03"));
        cll = create_title_table(dateweektoString(new Date()),(
                                        " "+"\n"+
                                        strlist.PDFheader.get(3) + " - " + newitog.date_open.getHours() + ":" +
                                                minutes(""+newitog.date_open.getMinutes()) + "\t" +
                                        "Время отпр. отчёта - " + dfropen.GetDate().getHours() + ":" +
                                                minutes(""+dfropen.GetDate().getMinutes())+"\n"+
                                        " "+"\n"+
                                        strlist.PDFheader.get(4) + " - " + newitog.date_close.getHours() + ":" +
                                                minutes(""+newitog.date_close.getMinutes()) + "\t" +
                                        "Время отпр. отчёта - " + dfrclose.GetDate().getHours() + ":" +
                                                minutes(""+dfrclose.GetDate().getMinutes())
                                        ).split("\n"),
                                        cll, BW, wtm);
        nestedTable.addCell(cll);//03///////////////////////////////////////////
        cll = new PdfPCell(new Paragraph("1"));
        //
        cll=create_big_table(strlist, dfropen, dfrdrug, dfrsteal, dfrclose, 0, cll);
        cll.addElement(create_table( ("Всего мешков (шт)\t" +
                                        (int)newitog.amountbag[0] + "\t" +
                                        (int)newitog.amountbag[1] + "\t" +
                                        (int)newitog.amountbag[2] + "\t" +
                                        (int)newitog.amountbag[3]
                                        ).split("\t") , font, Element.ALIGN_CENTER,
                                        1, wtl));
        //
        cll.addElement(create_table( (strlist.DataSale.get(1)
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wtl));
        cll.addElement(create_table( (strlist.SessionTypes.get(0)+"\t"+
                                      strlist.SessionTypes.get(1)+"\t"+
                                      strlist.SessionTypes.get(2)+"\t"+
                                      strlist.SessionTypes.get(3)+"\t"+
                                      strlist.SessionTypes.get(4)
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      2, wtl));
        for(int i=0;i<strlist.DataSubSale.get(1).split("\t").length;i++)
        {
            cll.addElement(create_table( (strlist.DataSubSale.get(1).split("\t")[i]+"\t"+
                                      dfropen.matrix[1][i]+"\t"+
                                      dfrdrug.matrix[1][i]+"\t"+
                                      dfrsteal.matrix[1][i]+"\t"+
                                      dfrclose.matrix[1][i]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, wtl));
        }
        cll.addElement(create_table( ("ВЕС В СКЛАД.ХОЛ.\t" +
                                        newitog.amountbag[0] * 2400 + "\t" +
                                        newitog.amountbag[1] * 2400 + "\t" +
                                        newitog.amountbag[2] * 2400 + "\t" +
                                        newitog.amountbag[3] * 2400
                                        ).split("\t") , font, Element.ALIGN_CENTER,
                                        1, wtl));
        cll.addElement(create_table( ("ВСЕГО\t" +
                                        (newitog.weightall[0]+newitog.amountbag[0] * 2400) + "\t" +
                                        (newitog.weightall[1]+newitog.amountbag[1] * 2400) + "\t" +
                                        (newitog.weightall[2]+newitog.amountbag[2] * 2400) + "\t" +
                                        (newitog.weightall[3]+newitog.amountbag[3] * 2400)
                                        ).split("\t") , font, Element.ALIGN_CENTER,
                                        1, wtl));
        //
        cll.addElement(create_table( ("ВЕС ВСЕГО ПРОДАННОГО МОРОЖЕННОГО ИЗ УЧЁТА оставшегося мороженного -"
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wts));
        cll.addElement(create_table( (" " + (newitog.weightall[0]+newitog.amountbag[0] * 2400 +
                                        newitog.weightall[1]+newitog.amountbag[1] * 2400 -
                                        newitog.weightall[2]-newitog.amountbag[2] * 2400 -
                                        newitog.weightall[3]-newitog.amountbag[3] * 2400)
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        1, wts));
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//1///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("2"));
        //
        cll.addElement(create_table( (" "
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wtm));
        cll.addElement(create_table( ("Выручка"+"\t"+
                                        (newitog.amount_s*newuser.price_s + 
                                        newitog.amount_k*newuser.price_k + 
                                        newitog.amount_t*newuser.price_t)
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( ("Касса/открытие"+"\t"+
                                        dfropen.getCash()
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( ("Касса/закрытие"+"\t"+
                                        dfrclose.getCash()
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        if(newitog.get_DC().length>0)
        {
            cll.addElement(create_table( ("Выплачено с кассы"
                                            ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                            0, wts));
        }
        for (DataCass _dc : newitog.get_DC())
        {
            cll.addElement(create_table((strlist.DataCass.get(_dc.getTypeEvent().ordinal()) + ": " +
                                        _dc.getFam() + "\t" +
                                        _dc.getCash()
                                        ).split("\t"), font, Element.ALIGN_LEFT,
                                        1, wts));
        }
        cll.addElement(create_table( ("Всего выручка: " + (newitog.amount_s*newuser.price_s + 
                                        newitog.amount_k*newuser.price_k + 
                                        newitog.amount_t*newuser.price_t)
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wts));
        cll.addElement(create_table( ("Выручка"+"\t"+
                                        newitog.amount_s*newuser.price_s+"\t"+
                                        newitog.amount_k*newuser.price_k+"\t"+
                                        newitog.amount_t*newuser.price_t
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( (" \t" +
                                        "Стаканчик\t" +
                                        "Кепка\t" +
                                        "Термос"
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( (strlist.DataSale.get(3)+" и Кепки"
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wts));
            cll.addElement(create_table( (strlist.SessionTypes.get(0+1)+"\t"+
                                      (int)dfropen.matrix[3][0]+"\t"+
                                      (int)dfropen.getsumm(4)+"\t"+
                                      (int)dfropen.matrix[3][1]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, wts));
            cll.addElement(create_table( (strlist.SessionTypes.get(1+1)+"\t"+
                                      (int)dfrdrug.matrix[3][0]+"\t"+
                                      (int)dfrdrug.getsumm(4)+"\t"+
                                      (int)dfrdrug.matrix[3][1]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, wts));
            cll.addElement(create_table( (strlist.SessionTypes.get(2+1)+"\t"+
                                      (int)dfrsteal.matrix[3][0]+"\t"+
                                      (int)dfrsteal.getsumm(4)+"\t"+
                                      (int)dfrsteal.matrix[3][1]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, wts));
            cll.addElement(create_table( (strlist.SessionTypes.get(3+1)+"\t"+
                                      (int)dfrclose.matrix[3][0]+"\t"+
                                      (int)dfrclose.getsumm(4)+"\t"+
                                      (int)dfrclose.matrix[3][1]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, wts));
        cll.addElement(create_table( ("Продано штук"+"\t"+
                                        (int)newitog.amount_s+"\t"+
                                        (int)newitog.amount_k+"\t"+
                                        (int)newitog.amount_t
                                        ).split("\t") , font, Element.ALIGN_CENTER,
                                        1, wts));
        cll.addElement(create_table( (" \t" +
                                        "Стаканчик\t" +
                                        "Кепка\t" +
                                        "Термос"
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( ("Вес проданных стаканчиков/кепок/термосов: "+
                                        (newitog.amount_s*newuser.weight_s + 
                                        newitog.amount_k*newuser.weight_k + 
                                        newitog.amount_t*newuser.weight_t)
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wts));
        cll.addElement(create_table( ("Вес"+"\t"+
                                        newitog.amount_s*newuser.weight_s+"\t"+
                                        newitog.amount_k*newuser.weight_k+"\t"+
                                        newitog.amount_t*newuser.weight_t
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( (" \t" +
                                        "Стаканчик\t" +
                                        "Кепка\t" +
                                        "Термос"
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( ("ВЕС ВСЕГО ПРОДАННОГО МОРОЖЕННОГО ИЗ УЧЁТА СТАКАНОВ, КЕПОК И ТЕРМОСОВ - "
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wts));
        cll.addElement(create_table( (" " + (newitog.amount_s*newuser.weight_s + 
                                        newitog.amount_k*newuser.weight_k + 
                                        newitog.amount_t*newuser.weight_t)
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        1, wts));
        for(int i=0;i<newitog.get_mulct().length;i++)
        {
            cll.addElement(create_table( ("ШТРАФ - "+newitog.get_mulct_str()[i]+"\t"+
                                            newitog.get_mulct()[i]
                                            ).split("\t") , font, Element.ALIGN_LEFT,
                                            1, wts));
        }
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//2///////////////////////////////////////////
        cll = new PdfPCell(new Paragraph("3"));
        //
        cll.addElement(create_table( (" "
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wtm));
        cll.addElement(create_table( (strlist.DataSale.get(5) + "\t" +
                                        "ОТКРЫТИЕ\t" +
                                        "ЗАКРЫТИЕ"
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( (strlist.DataSubSale.get(5).split("\t")[0] + "\t" +
                                        dfropen.matrix[5][0] + "\t" +
                                        dfrclose.matrix[5][0]
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( (strlist.DataSubSale.get(5).split("\t")[1] + "\t" +
                                        dfropen.matrix[5][1] + "\t" +
                                        dfrclose.matrix[5][1]
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll=create_big_table(strlist, dfropen, dfrdrug, dfrsteal, dfrclose, 2, cll);
        cll=create_big_table(strlist, dfropen, dfrdrug, dfrsteal, dfrclose, 4, cll);
        cll.addElement(create_table( ("Заработанные деньги"
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wtm));
        cll.addElement(create_table( ("За время работы"+"\t"+
                                        newitog.salary
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( ("Плюс за проценты"+"\t"+
                                        ((double)newitog.amount_k*newuser.price_k / 100) * newuser.bonus
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( ("Всего"+"\t"+
                                        (newitog.salary + ((double)newitog.amount_k*newuser.price_k / 100) * newuser.bonus)
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( ("Штраф"+"\t"+
                                        newitog.get_summ_mulct()
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( ("Итого"+"\t"+
                                        API.my_round((newitog.salary +
                                        ((double)newitog.amount_k*newuser.price_k / 100) * newuser.bonus -
                                        newitog.get_summ_mulct()),2)
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//3///////////////////////////////////////////
        //
        document.add(nestedTable);
        document.close();
    }
    
    public static void _CreatePDF(Strings strlist,
            user newuser,
            DataForRecord dfropen,
            Itog newitog,
            String name) throws DocumentException, FileNotFoundException
    {
        int wts = 88;
        int wtm = 99;
        int BW = 0;
            createpdf(name);
        PdfPTable nestedTable = new PdfPTable(3);
        nestedTable.setWidthPercentage(wtm);
        PdfPCell cll;
        //title///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("01"));
        cll = create_title_table(newitog.nameshop,(
                                        strlist.PDFheader.get(0)+"\n"+
                                        newuser.GetSurname() + " " + newuser.GetName() + " " + newuser.GetPatronymic()+"\n"+
                                        "Точка"+"\n"+
                                        newitog.nameshop
                                        ).split("\n"),
                                        cll, BW, wtm);
        nestedTable.addCell(cll);//01///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("02"));
        cll = create_title_table(dateonly(new Date()),(
                                        strlist.PDFheader.get(1)+"\n"+
                                        dfropen.GetX() + " " + dfropen.GetY()+"\n"+
                                        strlist.PDFheader.get(2)+"\n"+
                                        datetostring(newitog.date_open)
                                        ).split("\n"),
                                        cll, BW, wtm);
        nestedTable.addCell(cll);//02///////////////////////////////////////////
        cll = new PdfPCell(new Phrase("03"));
        cll = create_title_table(dateweektoString(new Date()),(
                                        " "+"\n"+
                                        strlist.PDFheader.get(3) + " - " + newitog.date_open.getHours() + ":" +
                                                minutes(""+newitog.date_open.getMinutes()) + "\t" +
                                        "Время отпр. отчёта - " + dfropen.GetDate().getHours() + ":" +
                                                minutes(""+dfropen.GetDate().getMinutes())
                                        ).split("\n"),
                                        cll, BW, wtm);
        nestedTable.addCell(cll);//03///////////////////////////////////////////
        cll = new PdfPCell(new Paragraph("1"));
        cll=create_big_table(strlist, dfropen, 0, cll);
        cll.addElement(create_table( ("Всего мешков (шт)\t" +
                                        (int)newitog.amountbag[0]
                                        ).split("\t") , font, Element.ALIGN_CENTER,
                                        1, wts));
        //
        cll.addElement(create_table( (strlist.DataSale.get(1)
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wts));
        cll.addElement(create_table( (strlist.SessionTypes.get(0)+"\t"+
                                      strlist.SessionTypes.get(1)
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      2, wts));
        for(int i=0;i<strlist.DataSubSale.get(1).split("\t").length;i++)
        {
            cll.addElement(create_table( (strlist.DataSubSale.get(1).split("\t")[i]+"\t"+
                                      dfropen.matrix[1][i]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, wts));
        }
        cll.addElement(create_table( ("ВЕС В СКЛАД.ХОЛ.\t" +
                                        newitog.amountbag[0] * 2400
                                        ).split("\t") , font, Element.ALIGN_CENTER,
                                        1, wts));
        cll.addElement(create_table( ("ВСЕГО\t" +
                                        (newitog.weightall[0]+newitog.amountbag[0] * 2400)
                                        ).split("\t") , font, Element.ALIGN_CENTER,
                                        1, wts));
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//1///////////////////////////////////////////
        cll = new PdfPCell(new Paragraph("2"));
        cll.addElement(create_table( (" "
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wtm));
        cll.addElement(create_table( ("Касса/открытие"+"\t"+
                                        dfropen.getCash()
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( (strlist.DataSale.get(3)+" и Кепки"
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wts));
            cll.addElement(create_table( (strlist.SessionTypes.get(1)+"\t"+
                                      (int)dfropen.matrix[3][0]+"\t"+
                                      (int)dfropen.getsumm(4)+"\t"+
                                      (int)dfropen.matrix[3][1]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, wts));
        cll.addElement(create_table( (" \t" +
                                        "Стаканчик\t" +
                                        "Кепка\t" +
                                        "Термос"
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//2///////////////////////////////////////////
        cll = new PdfPCell(new Paragraph("3"));
        cll.addElement(create_table( (" "
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wtm));
        cll.addElement(create_table( (strlist.DataSale.get(5) + "\t" +
                                        "ОТКРЫТИЕ"
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( (strlist.DataSubSale.get(5).split("\t")[0] + "\t" +
                                        dfropen.matrix[5][0]
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll.addElement(create_table( (strlist.DataSubSale.get(5).split("\t")[1] + "\t" +
                                        dfropen.matrix[5][1]
                                        ).split("\t") , font, Element.ALIGN_LEFT,
                                        1, wts));
        cll=create_big_table(strlist, dfropen, 2, cll);
        cll=create_big_table(strlist, dfropen, 4, cll);
        cll.setBorderWidth(BW);
        nestedTable.addCell(cll);//6///////////////////////////////////////////
        document.add(nestedTable);
        document.close();
    }
    //
    private static PdfPCell create_big_table(Strings strlist,
                                                DataForRecord dfropen,
                                                DataForRecord dfrdrug,
                                                DataForRecord dfrsteal,
                                                DataForRecord dfrclose,
                                                int num, PdfPCell cll) throws DocumentException
    {
        cll.addElement(create_table( (strlist.DataSale.get(num)
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, 111));
        cll.addElement(create_table( (strlist.SessionTypes.get(0)+"\t"+
                                      strlist.SessionTypes.get(1)+"\t"+
                                      strlist.SessionTypes.get(2)+"\t"+
                                      strlist.SessionTypes.get(3)+"\t"+
                                      strlist.SessionTypes.get(4)
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      2, 111));
        for(int i=0;i<strlist.DataSubSale.get(num).split("\t").length;i++)
        {
            cll.addElement(create_table( (strlist.DataSubSale.get(num).split("\t")[i]+"\t"+
                                      (int)dfropen.matrix[num][i]+"\t"+
                                      (int)dfrdrug.matrix[num][i]+"\t"+
                                      (int)dfrsteal.matrix[num][i]+"\t"+
                                      (int)dfrclose.matrix[num][i]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, 111));
        }
        return cll;
    }
    private static PdfPCell create_big_table(Strings strlist,
                                                DataForRecord dfropen,
                                                int num, PdfPCell cll) throws DocumentException
    {
        cll.addElement(create_table( (strlist.DataSale.get(num)
                                        ).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, 88));
        cll.addElement(create_table( (strlist.SessionTypes.get(0)+"\t"+
                                      strlist.SessionTypes.get(1)
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      2, 88));
        for(int i=0;i<strlist.DataSubSale.get(num).split("\t").length;i++)
        {
            cll.addElement(create_table( (strlist.DataSubSale.get(num).split("\t")[i]+"\t"+
                                      (int)dfropen.matrix[num][i]
                                      ).split("\t") , font, Element.ALIGN_CENTER,
                                      1, 88));
        }
        return cll;
    }
    //
    private static PdfPCell create_title_table(String title,
                                                String s[],
                                                PdfPCell cll,int bw, int wp) throws DocumentException
    {
        cll.addElement(create_table( (title).split("\t") , fonttitle, Element.ALIGN_LEFT,
                                        0, wp));
        int iter=1;
        for (String item : s)
        {
            iter=(iter*(-1))+1;
            cll.addElement(create_table((item).split("\t"), font, Element.ALIGN_LEFT + iter, Element.ALIGN_LEFT + iter, wp));
        }
        cll.setBorderWidth(bw);
        return cll;
    }
    private static PdfPTable create_table(String[] str, Font f,int align ,int bw, int wp) throws DocumentException
    {
        PdfPTable table = new PdfPTable(str.length);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell cell;
        for (String str1 : str)
        {
            cell = new PdfPCell(new Phrase(str1, f));
            cell.setHorizontalAlignment(align);
            cell.setBorderWidth(bw);
            table.addCell(cell);
        }
        table.setWidthPercentage(wp);
        return table;
    }
    //
    private static void createpdf(String pdfname) throws DocumentException, FileNotFoundException
    {
        document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(pdfname + ".pdf"));
        document.open();
    }
    private static void setfont(String fonts) throws DocumentException, IOException
    {
        BaseFont bf = BaseFont.createFont(fonts, BaseFont.IDENTITY_H, true);
        font = new Font(bf, 8, Font.NORMAL);
        fonttitle = new Font(bf, 12, 2);
    }
    //
    static double getresultmass(double[] open, double[] drug, double[] steal, double[] close, int i)
    {
        return open[i] + drug[i] - steal[i] - close[i];
    }
    static String[] getmass(List<String> strl, int i)
    {
        return strl.toArray(new String[strl.size()])[i].split("\t");
    }
    static double[] getmass(double[] open, double[] drug, double[] steal, double[] close, int i)
    {
        double[] c = new double[4];
        c[0] = open[i];
        c[1] = drug[i];
        c[2] = steal[i];
        c[3] = close[i];
        return c;
    }
    //
    static public String minutes(String s)
    {
        if (s.length() == 1)
        {
            s = "0" + s;
        }
        return s;
    }
    static String datetostring(Date today)
    {
        return today.getDate() + " " + monthtoString(today.getMonth() + 1) + " " + (1900 + today.getYear()) + " " + API.weektoString(today.getDay());
    }

    static String dateweektoString(Date today)
    {
        return API.weektoString(today.getDay());
    }

    static String dateonly(Date today)
    {
        return today.getDate() + " " + monthtoString(today.getMonth() + 1) + " " + (1900 + today.getYear());
    }
    
    static private String monthtoString(int w)
    {
        switch (w)
        {
            case 1:
                return "Января";
            case 2:
                return "Февраля";
            case 3:
                return "Марта";
            case 4:
                return "Апреля";
            case 5:
                return "Мая";
            case 6:
                return "Июня";
            case 7:
                return "Июля";
            case 8:
                return "Августа";
            case 9:
                return "Сентября";
            case 10:
                return "Октября";
            case 11:
                return "Ноября";
            case 12:
                return "Декабря";
        }
        return "fail";
    }
}