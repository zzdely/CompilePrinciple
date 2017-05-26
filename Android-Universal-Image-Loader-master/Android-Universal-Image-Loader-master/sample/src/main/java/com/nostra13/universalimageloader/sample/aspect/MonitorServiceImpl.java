package com.nostra13.universalimageloader.sample.aspect;

/**
 * Created by 50567 on 17/5/18.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;

//import java.lang.management.ManagementFactory;

//import com.sun.management.OperatingSystemMXBean;

/** */

/**
 * ��ȡϵͳ��Ϣ��ҵ���߼�ʵ����.
 *
 * @author amg * @version 1.0 Creation date: 2008-3-11 - ����10:06:06
 */
public class MonitorServiceImpl implements IMonitorService {
    // �������ó�Щ����ֹ�������д˴�ϵͳ���ʱ��cpuռ���ʣ��Ͳ�׼��
    private static final int CPUTIME = 5000;

    private static final int PERCENT = 100;

    private static final int FAULTLENGTH = 10;

    /** */
    /**
     * ��õ�ǰ�ļ�ض���.
     *
     * @return ���ع���õļ�ض���
     * @throws Exception
     * @author amg * Creation date: 2008-4-25 - ����10:45:08
     */
    public MonitorInfoBean getMonitorInfoBean() throws Exception {
        int kb = 1024;

        // ��ʹ���ڴ�
        long totalMemory = Runtime.getRuntime().totalMemory() / kb;
        // ʣ���ڴ�
        long freeMemory = Runtime.getRuntime().freeMemory() / kb;
        // ����ʹ���ڴ�
        long maxMemory = Runtime.getRuntime().maxMemory() / kb;

        //OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // ����ϵͳ
        String osName = System.getProperty("os.name");
        // �ܵ������ڴ�
        //long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / kb;
        // ʣ��������ڴ�
        //long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
        // ��ʹ�õ������ڴ�
        //long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / kb;

        // ����߳�����
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent())
            ;
        int totalThread = parentThread.activeCount();

        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = this.getCpuRatioForWindows();
        }

//        if (osName.toLowerCase().startsWith("linux")) {
//            cpuRatio = this.getCpuRateForLinux();
//        }

        // ���췵�ض���
        MonitorInfoBean infoBean = new MonitorInfoBean();
        infoBean.setFreeMemory(freeMemory);
        //infoBean.setFreePhysicalMemorySize(freePhysicalMemorySize);
        infoBean.setMaxMemory(maxMemory);
        infoBean.setOsName(osName);
        infoBean.setTotalMemory(totalMemory);
        //infoBean.setTotalMemorySize(totalMemorySize);
        infoBean.setTotalThread(totalThread);
        //infoBean.setUsedMemory(usedMemory);
        infoBean.setCpuRatio(cpuRatio);
        return infoBean;
    }

    /** */
    /**
     * ���CPUʹ����.
     *
     * @return ����cpuʹ����
     * @author amg * Creation date: 2008-4-25 - ����06:05:11
     */
    private double getCpuRateForLinux(){
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader brStat = null;
        StringTokenizer tokenStat = null;
        try{
            Process process = Runtime.getRuntime().exec("top -b -n 1");
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            brStat = new BufferedReader(isr);
            brStat.readLine();
            brStat.readLine();
            tokenStat = new StringTokenizer(brStat.readLine());
            tokenStat.nextToken();
            tokenStat.nextToken();
            tokenStat.nextToken();
            tokenStat.nextToken();
            String cpuUsage = tokenStat.nextToken();
            System.out.println("CPU idle : "+cpuUsage);
            Float usage = new Float(cpuUsage.substring(0,cpuUsage.indexOf("%")));
            return (100-usage.floatValue());
        } catch(IOException ioe){
            System.out.println(ioe.getMessage());
            freeResource(is, isr, brStat);
            return 1;
        } finally{
            freeResource(is, isr, brStat);
        }

    }
    private static void freeResource(InputStream is, InputStreamReader isr, BufferedReader br){
        try{
            if(is!=null)
                is.close();
            if(isr!=null)
                isr.close();
            if(br!=null)
                br.close();
        }catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }
    }


    private double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir") + "//system32//wbem//wmic.exe process get Caption,CommandLine,"
                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // ȡ������Ϣ
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(PERCENT * (busytime) / (busytime + idletime)).doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    /** */
    /**
     * ��ȡCPU��Ϣ.
     *
     * @param proc
     * @return
     * @author amg * Creation date: 2008-4-25 - ����06:10:14
     */
    private long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // �ֶγ���˳��Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = Bytes.substring(line, capidx, cmdidx - 1).trim();
                String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                // log.info("line="+line);
                if (caption.equals("System Idle Process") || caption.equals("System")) {
                    idletime += Long.valueOf(Bytes.substring(line, kmtidx, rocidx - 1).trim()).longValue();
                    idletime += Long.valueOf(Bytes.substring(line, umtidx, wocidx - 1).trim()).longValue();
                    continue;
                }

                kneltime += Long.valueOf(Bytes.substring(line, kmtidx, rocidx - 1).trim()).longValue();
                usertime += Long.valueOf(Bytes.substring(line, umtidx, wocidx - 1).trim()).longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** */
    /**
     * ���Է���.
     *
     * @param args
     * @throws Exception
     * @author amg * Creation date: 2008-4-30 - ����04:47:29
     */

}
