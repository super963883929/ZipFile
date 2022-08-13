# ZipFile
🔥 🔥Android文件压缩解压缩，支持多文件压缩解压缩、密码压缩解压缩Android file compression and decompression, support multi-file compression and decompression, password compression and decompression


## ·压缩
```
    /**
     * 压缩文件或者文件夹
     *
     * @param targetPath          被压缩的文件路径
     * @param destinationFilePath 压缩后生成的文件路径
     * @param callback            压缩进度回调
     */
    public static void zipFile(String targetPath, String destinationFilePath, ZipCallback callback) {
    ...
    }
    
    
    /**
     * 压缩文件或者文件夹
     *
     * @param targetPath          被压缩的文件路径
     * @param destinationFilePath 压缩后生成的文件路径
     * @param password            压缩加密 密码
     * @param callback            压缩进度回调
     */
    public static void zipFile(String targetPath, String destinationFilePath, String password, ZipCallback callback) {
     ...
    }
    
    
    /**
     * 压缩多个文件
     *
     * @param list                被压缩的文件集合
     * @param destinationFilePath 压缩后生成的文件路径
     * @param password            压缩 密码
     * @param callback            回调
     */
    public static void zipFiles(ArrayList<File> list, String destinationFilePath, String password, final ZipCallback callback) {
     ...
    }
    
    
    
     /**
     * 压缩多个文件
     * @param list                被压缩的文件集合
     * @param destinationFilePath 压缩后生成的文件路径
     * @param callback            压缩进度回调
     */
    public static void zipFiles(ArrayList<File> list, String destinationFilePath, ZipCallback callback) {
       ...
    }
    
    
```
    
    
   
   
   
## ·解压
```
 /**
     * 解压
     *
     * @param targetZipFilePath     待解压目标文件地址
     * @param destinationFolderPath 解压后文件夹地址
     * @param callback              回调
     */
    public static void unzip(String targetZipFilePath, String destinationFolderPath, ZipCallback callback) {
       。。。
    }

    /**
     * 解压
     *
     * @param targetZipFilePath     待解压目标文件地址
     * @param destinationFolderPath 解压后文件夹地址
     * @param password              解压密码
     * @param callback              回调
     */
    public static void unzip(String targetZipFilePath, String destinationFolderPath, String password, final ZipCallback callback) {
       。。。
    }


   
