package aspect;

/**
 * Created by 50567 on 17/5/18.
 */

/**
 * byte������.
 *
 * @author <a href="mailto:xiexingxing1121@126.com">AmigoXie</a>
 * @version 1.0 Creation date: 2008-4-30 - ����04:57:23
 */
public class Bytes {
    /**
     * ����String.subString�Ժ��ִ���������⣨��һ��������Ϊһ���ֽ�)������� �������ֵ��ַ���ʱ�����������ֵ������£�
     *
     * @param src
     *            Ҫ��ȡ���ַ���
     * @param start_idx
     *            ��ʼ���꣨����������)
     * @param end_idx
     *            ��ֹ���꣨���������꣩
     * @return
     */
    public static String substring(String src, int start_idx, int end_idx) {
        byte[] b = src.getBytes();
        String tgt = "";
        for (int i = start_idx; i <= end_idx; i++) {
            tgt += (char) b[i];
        }
        return tgt;
    }
}
