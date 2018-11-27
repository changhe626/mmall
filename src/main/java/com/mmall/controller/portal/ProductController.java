package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by geely
 */

@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;



    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    /**
     * 改造为restful
     * @param productId
     * @return
     */
    @RequestMapping(value = "/{productID}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail2(@PathVariable("productID") Integer productId){
        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }



    @RequestMapping(value = "/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list2(@PathVariable(value = "keyword")String keyword,
                                         @PathVariable(value = "categoryId")Integer categoryId,
                                         @PathVariable(value = "pageNum") Integer pageNum,
                                         @PathVariable(value = "pageSize") Integer pageSize,
                                         @PathVariable(value = "orderBy") String orderBy){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=20;
        }
        if(orderBy==null){
            orderBy="price_asc";
        }

        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }

    /**
     * 然后list3   和  list4  方法就分不清了.....
     * 不知道第一个参数是 1  是integer还是string了, 毕竟都可以匹配的,所以报错了
     * 在前面加上一个单词,作为分辨,然后识别出来...
     */

    /**
     * 只有 categoryId
     */
    @RequestMapping(value = "/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list3Bad(
                                          @PathVariable(value = "categoryId")Integer categoryId,
                                          @PathVariable(value = "pageNum") Integer pageNum,
                                          @PathVariable(value = "pageSize") Integer pageSize,
                                          @PathVariable(value = "orderBy") String orderBy){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=20;
        }
        if(orderBy==null){
            orderBy="price_asc";
        }

        return iProductService.getProductByKeywordCategory("",categoryId,pageNum,pageSize,orderBy);
    }

    /**
     * 只有 categoryId
     */
    @RequestMapping(value = "category/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list6(
            @PathVariable(value = "categoryId")Integer categoryId,
            @PathVariable(value = "pageNum") Integer pageNum,
            @PathVariable(value = "pageSize") Integer pageSize,
            @PathVariable(value = "orderBy") String orderBy){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=20;
        }
        if(orderBy==null){
            orderBy="price_asc";
        }

        return iProductService.getProductByKeywordCategory("",categoryId,pageNum,pageSize,orderBy);
    }



    /**
     * 只有keyword 了....
     */
    @RequestMapping(value = "/{keyword}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list4Bad(@PathVariable(value = "keyword")String keyword,
                                          @PathVariable(value = "pageNum") Integer pageNum,
                                          @PathVariable(value = "pageSize") Integer pageSize,
                                          @PathVariable(value = "orderBy") String orderBy){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=20;
        }
        if(orderBy==null){
            orderBy="price_asc";
        }

        return iProductService.getProductByKeywordCategory(keyword,null,pageNum,pageSize,orderBy);
    }


    /**
     * 这个keyword 加上,才可以,作为分辨用
     * 只有keyword 了....
     */
    @RequestMapping(value = "keyword/{keyword}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> list5(@PathVariable(value = "keyword")String keyword,
                                          @PathVariable(value = "pageNum") Integer pageNum,
                                          @PathVariable(value = "pageSize") Integer pageSize,
                                          @PathVariable(value = "orderBy") String orderBy){
        if(pageNum==null){
            pageNum=1;
        }
        if(pageSize==null){
            pageSize=20;
        }
        if(orderBy==null){
            orderBy="price_asc";
        }

        return iProductService.getProductByKeywordCategory(keyword,null,pageNum,pageSize,orderBy);
    }





}
