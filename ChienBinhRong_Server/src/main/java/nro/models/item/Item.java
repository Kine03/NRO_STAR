package nro.models.item;

import nro.consts.ConstItem;

import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstCombine;
import nro.data.ItemData;
import nro.services.InventoryService;

public class Item {

    private static final ItemOption OPTION_NULL = new ItemOption(73, 0);

    public ItemTemplate template;

    public String info;

    public String content;

    public int quantity;

    public List<ItemOption> itemOptions;

    public long createTime;

    public boolean isNotNullItem() {
        return this.template != null;
    }

    public Item() {
        this.itemOptions = new ArrayList<>();
        this.createTime = System.currentTimeMillis();
    }

    public String getInfo() {
        String strInfo = "";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString();
        }
        return strInfo;
    }

    public String getInfoItem() {
        String strInfo = "|1|" + template.name + "\n|0|";
        for (ItemOption itemOption : itemOptions) {
            strInfo += itemOption.getOptionString() + "\n";
        }
        strInfo += "|2|" + template.description;
        return strInfo;
    }

    public List<ItemOption> getDisplayOptions() {
        List<ItemOption> list = new ArrayList<>();
        if (itemOptions.isEmpty()) {
            list.add(OPTION_NULL);
        } else {
            for (ItemOption o : itemOptions) {
                list.add(o.format());
            }
        }
        return list;
    }

    public String getContent() {
        return "Yêu cầu sức mạnh " + this.template.strRequire + " trở lên";
    }

    public boolean isItemKyGui() {
        for (ItemOption o : itemOptions) {
            int optionId = o.optionTemplate.id;
            if (optionId == 86 || optionId == 87) {
                return true;
            }
        }
        return false;
    }

    public boolean isTrangBiHSD() {
        return hasOptionTemplateId(this, 93);
    }

    public boolean isCongThuc() {
        if (this.template.id >= 1071 && this.template.id <= 1073) {
            return true;
        }
        return false;
    }

    public boolean hasOptionTemplateId(Item item, int optionTemplateId) {
        if (item.template.id != 884) {
            for (ItemOption option : item.itemOptions) {
                if (option.optionTemplate.id == optionTemplateId) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getQuantityStar() {
        if (!this.isNotNullItem()) {
            return 0;
        }
        return (int) this.itemOptions.stream()
                .filter(itemOption -> itemOption.optionTemplate.id == 107 || itemOption.optionTemplate.id == 102)
                .count();
    }

    public boolean itemCanRemoveOption() {
        if (this.template.type >= 5) {
            return false;
        }
        List<ItemOption> options = this.itemOptions;
        return options.stream().anyMatch(itemOption
                -> ConstCombine.itemOptionsCanRemove.contains(itemOption.optionTemplate.id)
        );
    }

    public boolean canConsign() {
        byte type = template.type;
        for (ItemOption o : itemOptions) {
            int optionId = o.optionTemplate.id;
            if (template.id != ConstItem.THOI_VANG && type != 5 && type == 12 || type == 33 || type == 29 || type == 27 || ((optionId == 86 || optionId == 87))) {
                return true;
            }
        }
        return false;
    }

    public boolean canConsign2() {
        byte type = template.type;
        for (ItemOption o : itemOptions) {
            int optionId = o.optionTemplate.id;
            if (type == 5) {
                return true;
            }
        }
        return false;
    }

    public void dispose() {
        this.template = null;
        this.info = null;
        this.content = null;
        if (this.itemOptions != null) {
            for (ItemOption io : this.itemOptions) {
                io.dispose();
            }
            this.itemOptions.clear();
        }
        this.itemOptions = null;
    }

    public short getId() {
        return template.id;
    }

    public byte getType() {
        return template.type;
    }

    public String getName() {
        return template.name;
    }

    public String Name() {

        return this.template.name;
    }

    public boolean isSKH() {
        for (ItemOption itemOption : itemOptions) {
            if (itemOption.optionTemplate.id >= 127 && itemOption.optionTemplate.id <= 135) {
                return true;
            }
        }
        return false;
    }

    public boolean isDTS() {
        if (this.template.id >= 1048 && this.template.id <= 1062) {
            return true;
        }
        return false;
    }

    public boolean isManhTS() {
        if (this.template.id >= 1066 && this.template.id <= 1070) {
            return true;
        }
        return false;
    }

    public boolean isThucAn() {
        if (this.template.id >= 663 && this.template.id <= 667) {
            return true;
        }
        return false;
    }

    public boolean isDTL() {
        if (this.template.id >= 555 && this.template.id <= 567) {
            return true;
        }
        return false;
    }

    public boolean isDHD() {
        if (this.template.id >= 650 && this.template.id <= 662) {
            return true;
        }
        return false;
    }

    public boolean isdanangcapDoTs() {
        if (this.template.id >= 1074 && this.template.id <= 1078) {
            return true;
        }
        return false;
    }

    public boolean isDamayman() {
        if (this.template.id >= 1079 && this.template.id <= 1083) {
            return true;
        }
        return false;
    }

    public boolean isCongthucVip() {
        if (this.template.id >= 1084 && this.template.id <= 1086) {
            return true;
        }
        return false;
    }

    public boolean isCongthucNomal() {
        if (this.template.id >= 1071 && this.template.id <= 1073) {
            return true;
        }
        return false;
    }

    public byte typeIdManh() {
        if (!isManhTS()) {
            return -1;
        }
        switch (this.template.id) {
            case 1066:
                return 0;
            case 1067:
                return 1;
            case 1070:
                return 2;
            case 1068:
                return 3;
            case 1069:
                return 4;
            default:
                return -1;
        }
    }

    public String typeName() {
        switch (this.template.type) {
            case 0:
                return "Áo";
            case 1:
                return "Quần";
            case 2:
                return "Găng";
            case 3:
                return "Giày";
            case 4:
                return "Rada";
            default:
                return "";
        }
    }

    public String typeHanhTinh() {
        switch (this.template.id) {
            case 1071:
                return "Trái đất";
            case 1084:
                return "Trái đất";
            case 1072:
                return "Namếc";
            case 1085:
                return "Namếc";
            case 1073:
                return "Xayda";
            case 1086:
                return "Xayda";
            default:
                return "";
        }
    }

    public String typeNameManh() {
        switch (this.template.id) {
            case 1066:
                return "Áo";
            case 1067:
                return "Quần";
            case 1070:
                return "Găng";
            case 1068:
                return "Giày";
            case 1069:
                return "Nhẫn";
            default:
                return "";
        }
    }

    public String typeDanangcap() {
        switch (this.template.id) {
            case 1074:
                return "cấp 1";
            case 1075:
                return "cấp 2";
            case 1076:
                return "cấp 3";
            case 1077:
                return "cấp 4";
            case 1078:
                return "cấp 5";
            default:
                return "";
        }
    }

    public String typeDaMayman() {
        switch (this.template.id) {
            case 1079:
                return "cấp 1";
            case 1080:
                return "cấp 2";
            case 1081:
                return "cấp 3";
            case 1082:
                return "cấp 4";
            case 1083:
                return "cấp 5";
            default:
                return "";
        }
    }
}
