package com.hervey.bos.web.action;

import com.hervey.bos.entity.base.*;
import com.hervey.bos.service.SubAreaService;
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

import javax.persistence.criteria.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 分区action
 * <p>
 * Created on 2017/12/11.
 *
 * @author hervey
 */
@Controller
@Scope(value = "prototype")
@ParentPackage("json-default")
@Namespace("/")
@Actions
public class SubAreaAction extends BaseAction<SubArea> {


    //分区服务层注入
    @Autowired
    private SubAreaService subAreaService;
    // 接收上传文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 批量分区数据导入
     */
    @Action(value = "subArea_import", results = {@Result(type = "redirect", location = "/pages/base/sub_area.html")})
    public String importData() throws Exception {
        List<SubArea> subAreas = new ArrayList<SubArea>();
        // 编写解析代码逻辑
        // 基于.xls 格式解析 HSSF
        // 1、 加载Excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        // 2、 读取一个sheet
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);//获取第一个sheet对象
        // 3、 读取sheet中每一行，一行数据 对应 一个区域对象
        for (Row row : sheet) {
            // 第一行表头跳过
            if (row.getRowNum() == 0) {
                // 第一行 跳过
                continue;
            }
            // 跳过空值的行，要求此行作废
            if (row.getCell(0) == null
                    || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            SubArea subArea = new SubArea();
            subArea.setId(row.getCell(0).getStringCellValue());//区域编号

            FixedArea fixedArea = new FixedArea();
            fixedArea.setId(row.getCell(1).getStringCellValue());
            subArea.setFixedArea(fixedArea);//定区编码

            Area area = new Area();
            area.setId(row.getCell(2).getStringCellValue());
            subArea.setArea(area);//区域编码

            subArea.setKeyWords(row.getCell(3).getStringCellValue());// 关键字
            subArea.setStartNum(row.getCell(4).getStringCellValue());// 初始化
            subArea.setEndNum(row.getCell(5).getStringCellValue());// 结束号

            subArea.setSingle(row.getCell(6).getStringCellValue().toCharArray()[0]);// 单双号
            subArea.setAssistKeyWords(row.getCell(7).getStringCellValue());// 辅助关键字（位置信息）


            subAreas.add(subArea);
        }
        // 调用业务层
        subAreaService.saveSubAreas(subAreas);

        return SUCCESS;
    }


    @Action(value = "subArea_findPage", results = {@Result(type = "json")})
    public String findPage() {
        Pageable pageable = new PageRequest(page - 1, rows);
        Specification<SubArea> specification = new Specification<SubArea>() {
            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = null;
                // list表示用来封装多个查询条件
                List<Predicate> list = new ArrayList<>();

                //// 收派标准（多表）
                //if (model.getStandard() != null && model.getStandard().getId() != null) {
                //    // 指定连接
                //    Join<Courier, Standard> join = root.join("standard", JoinType.INNER);// 相当于inner join t_courier c on
                //    Predicate p = cb.equal(join.get("id").as(Integer.class), model.getStandard().getId());
                //    list.add(p);
                //}
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getProvince())) {
                    Join<SubArea, Area> join = root.join("area", JoinType.INNER);//相当于inner join t_sub_area s on
                    Predicate p = cb.like(join.get("province").as(String.class), "%" + model.getArea().getProvince() + "%");
                    list.add(p);
                }
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getCity())) {
                    Join<SubArea, Area> join = root.join("area", JoinType.INNER);//相当于inner join t_sub_area s on
                    Predicate p = cb.like(join.get("city").as(String.class), "%" + model.getArea().getCity() + "%");
                    list.add(p);
                }
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getDistrict())) {
                    Join<SubArea, Area> join = root.join("area", JoinType.INNER);//相当于inner join t_sub_area s on
                    Predicate p = cb.like(join.get("district").as(String.class), "%" + model.getArea().getDistrict() + "%");
                    list.add(p);
                }
                if (model.getFixedArea() != null && StringUtils.isNotBlank(model.getFixedArea().getId())) {
                    Join<SubArea, Area> join = root.join("fixedArea", JoinType.INNER);//相当于inner join t_sub_area s on
                    Predicate p = cb.equal(join.get("id").as(String.class), model.getFixedArea().getId());
                    list.add(p);
                }

                // 快递员工号
                if (StringUtils.isNotBlank(model.getKeyWords())) {
                    // 相当于条件：courierNum = gh001;
                    Predicate p_key = cb.like(root.get("keyWords").as(String.class), model.getKeyWords());
                    list.add(p_key);
                    //Predicate p_assist = cb.like(root.get("assistKeyWords").as(String.class), model.getKeyWords());
                    //list.add(p_assist);
                }

                // 构建条件的组合
                if (list.size() > 0) {
                    Predicate[] predicates = new Predicate[list.size()]; // 构建一个数组，长度条件的长度
                    predicate = cb.and(list.toArray(predicates));// 将查询条件从集合中的值转成数组中的值

                }
                return predicate;
            }

        };
        Page<SubArea> page = subAreaService.findPage(specification, pageable);
        pushPageDataToValueStack(page);
        return SUCCESS;
    }
}
