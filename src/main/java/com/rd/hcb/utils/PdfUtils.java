//package com.rd.hcb.utils;
//
//import com.jacob.activeX.ActiveXComponent;
//import com.jacob.com.ComThread;
//import com.jacob.com.Dispatch;
//import com.jacob.com.Variant;
//
///**
// * @author zlx
// * @date 2019-11-12 17:12
// */
//public class PdfUtils {
//    public static void main(String[] args) {
//        pdfToWord("D:\\testword\\1#6005682019-10-31202142.pdf","D:\\testword\\");
//
//    }
//
//    private static void pdfToWord(String pdfpath , String docxpath) {
//        // PDF控制对象
//        Dispatch pdfObject = null;
//        // pdfActiveX PDDoc对象 主要建立PDF对象
//        ActiveXComponent app = null;
//        try {
//            ComThread.InitSTA();
//            app = new ActiveXComponent("AcroExch.PDDoc");
//            // 得到控制对象
//            pdfObject = app.getObject();
//            // 打开PDF文件，建立PDF操作的开始
//            Dispatch.call(pdfObject, "Open", new Variant(pdfpath));
//            Dispatch jSObject = Dispatch.invoke(pdfObject, "GetJSObject",
//                    Dispatch.Method, new Object[] {}, new int[1]).toDispatch();
//            Dispatch.invoke(jSObject, "SaveAs", Dispatch.Method, new Object[] {
//                            docxpath, new Variant("com.adobe.acrobat.docx") },
//                    new int[1]);
//        } catch (IllegalStateException ie) {
//
//        } catch (Exception e) {
//
//        } finally {
//            try {
//                // 关闭PDF
//                app.invoke("Close", new Variant[] {});
//            } catch (Exception e) {
//            }
//            try{
//                ComThread.Release();
//            }catch(Exception e){
//            }
//
//            try{
//                ComThread.Release();
//            }catch(Exception e){
//            }
//
//            // 杀死adobe进程
////            closePdfProcess();
//        }
//    }
//
//    /**
//     * 转换一个完成就杀死pdf acrobat进程
//     */
////    public static void closePdfProcess() {
////        try {
////            ExecutorUtil.syncExecute("taskkill.exe /F /IM acrobat.exe");
////            ExecutorUtil.syncExecute("taskkill.exe /F /IM acrotray.exe");
////        } catch (Exception e) {
////            // 此异常无实际业务作用
////        }
////    }
//}
