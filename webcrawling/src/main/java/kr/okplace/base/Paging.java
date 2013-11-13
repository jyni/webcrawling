package kr.okplace.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>com.firstavenue.model.Paging</h1>
 * <p>
 * </p>
 *
 * @since 2008. 01. 31
 * @version $Revision: 1.3 $
 * @author jynius($Author: dev05 $)
 */
public class Paging implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3181134046768955241L;
    
    /**
     * <p>���섏씠吏�떦 異쒕젰�섎뒗 湲��섏쓽 �좏깮媛�뒫��紐⑸줉.</p>
     */
    protected List<Integer> rowsOnAPageList;

    /**
     * <P>���섏씠吏�떦 異쒕젰�섎뒗 湲���</P>
     * <p>default 媛�10</p>
     */
    protected Integer rowsOnAPage = 10;

    /**
     * <p>�섏씠吏�紐⑸줉�먯꽌 蹂댁뿬以��섏씠吏���int)</p>
     */
    protected Integer pagesOnPaging = 10;

    /**
     * <p>珥�湲�닔</p>
     */
    protected Integer rowsTotal = null;

    /**
     * <p>�꾩옱 �섏씠吏�컪</p>
     * <p>default 0 : paging count from 0 and display 1</p>
     */
    protected Integer currPage = 0;

	/**
	 * <P>���섏씠吏�떦 異쒕젰�섎뒗 湲��섎� �좏깮�����덈뒗 紐⑸줉��諛섑솚�쒕떎.</P>
	 *
	 * @return RowsOnAPageList ���섏씠吏�떦 異쒕젰�섎뒗 湲���紐⑸줉
	 */
    public List<Integer> getRowsOnAPageList() {
    	
    	if(rowsOnAPageList==null) {
    		rowsOnAPageList = new ArrayList<Integer>();
    		rowsOnAPageList.add(10);
    		rowsOnAPageList.add(30);
    		rowsOnAPageList.add(50);
    	}
    	
    	return rowsOnAPageList;
    }

    /**
     * <p>���섏씠吏�떦 異쒕젰�섎뒗 湲��섎� �좏깮�����덈뒗 紐⑸줉���ㅼ젙�쒕떎.</p>
     * 
     * @param rowsOnAPageList
     */
    public void setRowsOnAPageList(List<Integer> rowsOnAPageList) {
    	this.rowsOnAPageList = rowsOnAPageList;
    }
    
	/**
	 * <P>���섏씠吏�떦 異쒕젰�섎뒗 湲��섎� 諛섑솚�쒕떎.</P>
	 *
	 * @return rowsOnAPage ���섏씠吏�떦 異쒕젰�섎뒗 湲���int)
	 */
    public Integer getRowsOnAPage() {
        return rowsOnAPage;
    }

	/**
	 * <P>���섏씠吏�떦 異쒕젰�섎뒗 湲����ㅼ젙�쒕떎.</P>
	 *
	 * @param rowsOnAPage ���섏씠吏�떦 異쒕젰�섎뒗 湲���int)
	 */
    public void setRowsOnAPage(Integer i) {
        rowsOnAPage = i;
    }
    
	/**
	 * <P>�섏씠吏�紐⑸줉�먯꽌 蹂댁뿬以��섏씠吏��섎� 諛섑솚�쒕떎.</P>
	 *
	 * @return nPagesAPaging �섏씠吏�紐⑸줉�먯꽌 蹂댁뿬以��섏씠吏���int)
	 */
    public int getPagesOnPaging() {
        return pagesOnPaging<1? 1: pagesOnPaging;
    }

	/**
	 * <P>�섏씠吏�紐⑸줉�먯꽌 蹂댁뿬以��섏씠吏��섎� �ㅼ젙�쒕떎.</P>
	 *
	 * @param nPagesAPaging �섏씠吏�紐⑸줉�먯꽌 蹂댁뿬以��섏씠吏���int)
	 */
    public void setPagesOnPaging(Integer i) {
        pagesOnPaging = i;
    }

	/**
	 * <P>�꾩옱 �섏씠吏�컪��諛섑솚�쒕떎.</P>
	 *
	 * @return �꾩옱 �섏씠吏�컪
	 */
    public Integer getCurrPage() {
        return currPage + 1;
    }

	/**
	 * <P>�꾩옱 �섏씠吏�컪���ㅼ젙�쒕떎.</P>
	 *
	 * @param currPage �꾩옱 �섏씠吏�int)
	 */
    public void setCurrPage(Integer i) {
        currPage = i - 1;
    }

    /**
     * <p>Paging����寃껋씤吏�� �먰븳�섍린 �꾪븳 媛�/p>
     * <p>rowsTotal媛믪씠 setting���섏뼱 �덈뒗吏��먮떒�쒕떎.</p>
     * 
     * @return
     */
    public boolean isPrepared() {
    	return rowsTotal!=null && rowsTotal>0;
    }
    
	/**
	 * <P>珥�湲�닔瑜�諛섑솚�쒕떎.</P>
	 *
	 * @return 珥�湲�닔
	 */
    public Integer getRowsTotal() {
        return rowsTotal;
    }

	/**
	 * <P>珥�湲�닔瑜��ㅼ젙�쒕떎.</P>
	 *
	 * @param nRows rows on total 珥�湲�닔(int)
	 */
    public void setRowsTotal(Integer i) {
        rowsTotal = i;
    }
    
    /**
	 * <P>�댁쟾�섏씠吏�留덉�留됱쨪��rownum��諛섑솚�쒕떎. �꾩옱�섏씠吏�� 泥ロ럹�댁���寃쎌슦 0��諛섑솚�쒕떎.</P>
	 * 
	 * @return �댁쟾�섏씠吏�留덉�留됱쨪��rownum
     */
    public Integer getOffset() {
    	return rowsOnAPage * currPage;
    }
    
    /**
	 * <P>�댁쟾�섏씠吏�留덉�留됱쨪��rownum��諛섑솚�쒕떎. �꾩옱�섏씠吏�� 泥ロ럹�댁���寃쎌슦 0��諛섑솚�쒕떎.</P>
	 * <p>alias of offset</p>
	 * 
	 * @return �댁쟾�섏씠吏�留덉�留됱쨪��rownum
     */
    public Integer getSkipRows() {
    	return rowsOnAPage * currPage;
    }

	/**
	 * <P>�꾩옱�섏씠吏�쓽 泥レ쨪��rownum��諛섑솚�쒕떎.</P>
	 * 
	 * @return startRow start row number(rownum)
	 */
    public Integer getStartRow() {
        return rowsOnAPage * currPage + 1;
    }

	/**
	 * <P>�꾩옱�섏씠吏�쓽 �앹쨪��rownum��諛섑솚�쒕떎.</P>
	 * 
	 * @return endRow end row number(rownum)
	 */
    public Integer getEndRow() {
    	
    	if(rowsTotal==null) {
    		return null;
    	}
    	
        int endRow = (this.currPage + 1) * this.rowsOnAPage;
        return (endRow < rowsTotal) ? endRow : rowsTotal;
    }

	/**
     * <p>�꾩옱 �섏씠吏�쓽 Row �섎� 諛섑솚�쒕떎.</p>
     * <p>alias of rowCount</p>
     * 
     * @return �꾩옱 �섏씠吏�쓽 Row ��
	 */
    public Integer getRows() {
        return getEndRow() - getStartRow() + 1;
    }
   
    /**
     * <p>�꾩옱 �섏씠吏�쓽 Row �섎� 諛섑솚�쒕떎.</p>
     * 
     * @return �꾩옱 �섏씠吏�쓽 Row ��
     */
    public Integer getRowCount() {
    	
    	// 留덉�留��섏씠吏��쇰㈃ 留덉�留��섏씠吏�쓽 row 媛쒖닔留�return �댁꽌 �ъ슜�쒕떎.
    	if(getCurrPage()==getPagesTotal()) {
    		return rowsTotal % rowsOnAPage;
    	}

    	return rowsOnAPage;
    }

	/**
	 * <P>珥��섏씠吏�닔瑜�諛섑솚�쒕떎.</P>
	 *
	 * @return 珥��섏씠吏�닔
	 */
    public Integer getPagesTotal() {

    	int pagesTotal = rowsTotal / rowsOnAPage;
        if ((rowsTotal % rowsOnAPage) > 0) {
            pagesTotal++;
        }

        return pagesTotal;
    }

	/**
	 * <P>�ㅼ쓬 �섏씠吏�컪��諛섑솚�쒕떎.</P>
	 * <p>�꾩옱 �섏씠吏�� 留덉�留��섏씠吏�씤 寃쎌슦, 留덉�留��섏씠吏�컪(�꾩옱�섏씠吏�컪)��諛섑솚�쒕떎.</p>
	 * 
	 * @return next Page
	 */
    public Integer getNextPage() {

        int last = getPagesTotal();
        int next = getCurrPage() + 1;

        return (next > last) ? last : next;
    }

	/**
	 * <P>�댁쟾 �섏씠吏�컪��諛섑솚�쒕떎.</P>
	 * <p>�꾩옱 �섏씠吏�� 泥ロ럹�댁���寃쎌슦, 1(�꾩옱�섏씠吏�컪)��諛섑솚�쒕떎.</p>
	 * 
	 * @return previous Page
	 */
    public Integer getPrevPage() {
       return (currPage<1) ? 1: currPage;
    }

	/**
	 * <P>�꾩옱�섏씠吏�ぉ濡앹쓽 泥ル쾲吏��섏씠吏�� 諛섑솚�쒕떎.</P>
	 * 
	 * @return startRow start row number(rownum)
	 */
    public Integer getStartPage() {
        return (this.currPage / getPagesOnPaging()) * getPagesOnPaging() + 1;
    }

	/**
	 * <P>�꾩옱�섏씠吏�ぉ濡앹쓽 留덉�留��섏씠吏�� 諛섑솚�쒕떎.</P>
	 * 
	 * @return endRow end row number(rownum)
	 */
    public Integer getEndPage() {

        int totalPage = getPagesTotal();
        int endPage = getStartPage() + getPagesOnPaging() - 1;

        return (endPage < totalPage)? endPage : totalPage;
    }

	/**
	 * <P>�꾩옱�섏씠吏�ぉ濡앹쓽 �섏씠吏�紐⑸줉��諛섑솚�쒕떎.</P>
	 * 
	 * @return
	 */
	public List<Integer> getPageList() {
		
		List<Integer> pages = new ArrayList<Integer>();
		int s = getStartPage();
		int e = getEndPage();
		for(int i=s; i<=e; i++) {
			pages.add(new Integer(i));
		}
		
		return pages;
	}

	/**
	 * <P>�꾩옱�섏씠吏�ぉ濡앹쓽 �섏씠吏�諛곗뿴���쒕떎.</P>
	 * 
	 * @return
	 */
	public Integer[] getPages() {
		
		int s = getStartPage();
		int e = getEndPage();
		Integer[] pages = new Integer[e-s+1];
		for(int i=0; i<=e-s; i++) {
			pages[i] = s + i;
		}
		
		return pages;
	}
}
