package com.hervey.bos.web.action;

import com.hervey.bos.entity.base.Area;
import com.hervey.bos.service.AreaService;
import com.hervey.bos.utils.PinYin4jUtils;
import com.hervey.bos.web.action.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/12/7.
 *
 * @author hervey
 */
@Controller
@Scope(value = "prototype")
@Actions
@ParentPackage("json-default")
@Namespace("/")
public class AreaAction extends BaseAction<Area> {

    //区域服务层注入
    @Autowired
    private AreaService areaService;


    // 接收上传文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 批量区域数据导入
     */
    @Action(value = "area_import", results = {@Result(type = "json")})
    public String importData() throws Exception {
        System.out.println("--> " + file);
        List<Area> areas = new ArrayList<>();
        // 编写解析代码逻辑
        // 基于.xls 格式解析 HSSF
        // 1、 加载Excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        // 2、 读取一个sheet
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        // 3、 读取sheet中每一行，一行数据 对应 一个区域对象
        for (Row row : sheet) {
            // 第一行表头跳过
            if (row.getRowNum() == 0) {
                // 第一行 跳过
                continue;
            }
            // 跳过空值的行，要求此行作废
            if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            Area area = new Area();
            area.setId(row.getCell(0).getStringCellValue());//区域编号
            area.setProvince(row.getCell(1).getStringCellValue());//省份
            area.setCity(row.getCell(2).getStringCellValue());//城市
            area.setDistrict(row.getCell(3).getStringCellValue());//区域
            area.setPostcode(row.getCell(4).getStringCellValue());//邮编

            /** 基于pinyin4j生成城市编码和简码 */
            String province = area.getProvince().substring(0, area.getProvince().length() - 1);//去掉省/市
            String city = area.getCity().substring(0, area.getCity().length() - 1);//去掉市
            String district = area.getDistrict().substring(0, area.getDistrict().length() - 1);//去掉区/县
            // 简码
            String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
            StringBuffer buffer = new StringBuffer();
            for (String str : headArray) {
                buffer.append(str);
            }
            area.setShortcode(buffer.toString());

            //城市编码
            String pinyin = PinYin4jUtils.hanziToPinyin(city, "");
            area.setCitycode(pinyin);
            areas.add(area);
        }


        // 调用业务层
        areaService.saveAreas(areas);
        return SUCCESS;
    }

    /**
     * 带有查询条件的分页查询
     *
     * @return List<Area> -> json
     * @throws Exception e
     */
    @Action(value = "area_page", results = {@Result(type = "json")})
    public String page() throws Exception {


        // 调用业务层 ，查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);

        // 定义Specification
        Specification<Area> specification = new Specification<Area>() {
            // 完成的条件查询

            /**
             * 传递：
             * 		Root<Area> root：（连接语句的时候需要字段，获取字段的名称）代表Criteria查询的根对象，Criteria查询的查询根定义了实体类型，能为将来导航获得想要的结果，它与SQL查询中的FROM子句类似
             * 		CriteriaQuery<?> query： （简单的查询可以使用CriteriaQuery()代表一个specific的顶层查询对象，它包含着查询的各个部分，比如：select 、from、where、group by、order by等
             * 		CriteriaBuilder cb：（复杂的查询可以使用CriteriaBuilder构建）用来构建CritiaQuery的构建器对象
             * 返回：Predicate：封装查询条件
             *
             */

            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = null;
                // list表示用来封装多个查询条件
                List<Predicate> list = new ArrayList<>();

                // 省份
                if (StringUtils.isNotBlank(model.getProvince())) {
                    Predicate p = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    list.add(p);
                }

                //城市
                if (StringUtils.isNotBlank(model.getCity())) {
                    Predicate p = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
                    list.add(p);
                }

                // 区域
                if (StringUtils.isNotBlank(model.getDistrict())) {
                    Predicate p = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    list.add(p);
                }


                // 构建条件的组合
                if (list.size() > 0) {
                    Predicate[] predicates = new Predicate[list.size()]; // 构建一个数组，长度条件的长度
                    predicate = cb.and(list.toArray(predicates));// 将查询条件从集合中的值转成数组中的值

                }
                return predicate;
            }
        };

        Page<Area> areaPage = areaService.findPageByCondition(pageable, specification);
        pushPageDataToValueStack(areaPage);
        return SUCCESS;
    }
}
