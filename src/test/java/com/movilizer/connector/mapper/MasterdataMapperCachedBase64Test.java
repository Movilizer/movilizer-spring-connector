package com.movilizer.connector.mapper;

import static org.hamcrest.MatcherAssert.assertThat;

public class MasterdataMapperCachedBase64Test {

//    SimpleBase64Model model;
//
//    String user = "user1";
//
//    String env = "test";
//
//    String pool = "simple_pool";
//
//    String keyName = "key";
//
//    String groupName = "group";
//
//    String descName = "desc";
//
//    String entryTitleName = "title";
//
//    String entryTitleFieldName = "imageTitle";
//
//    String entryImageName = "image";
//
//    String entryImageFieldName = "imageData";
//
//    String keyValue = "key value";
//
//    String groupValue = SimpleBase64Model.group;
//
//    String descValue = "image caption 1";
//
//    String entryTitleValue = "image title 1";
//
//    byte[] entryImageValue;
//
//    String imagePath = "/test-images/movilizer-logo.png";
//
//    String imageOutPath = "/test-images/movilizer-logo-out.png";
//
//    String base64Image = ""
//            + "iVBORw0KGgoAAAANSUhEUgAAADwAAAA8CAIAAAC1nk4lAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcd"
//            + "vqGQAAAVPSURBVGhD7Zj5K+VfGMfvIGtjSbIbQjSUbX6wxGWMGdkyI4aQIopsiWGyL6n5AWWZ1EiyNNEYlCJbDFkbaxFmcv+UeXef53667s"
//            + "Id3zGjb5/XT+c85znL59xnO1ciIiIiIiIiIiIi8r8kNDR0f39fJufo6Gh6erqtrU0qlRoYGLDGAyQ4OJhOrMKHDx9Y45/z9OnToaGh79+/e"
//            + "3h4kOT58+d8zOvU1dWRwj8mKSnp/PyczuTn50fChIQEkqiQnp5OCm5ubpmZmdT+2+DEV1dXdCAc3djYmORZWVkkVObHjx82Njak8OLFC0hw"
//            + "8X/bygMDA3EOOhDo6enhAYmkrKyMpUp8/PiRhyWSvLw8Ek5OTrq7u7P0vjExMVlbW6ONiYiICGH71tZWlirADwLT9/HxUVfAl1dVVWFBGrp"
//            + "HioqKeE856+vraWlpgk339fXxgILe3t6YmJjU1FRSGB4e5gEF8OOSkhJLS0tS+PPgVo6Pj3k3OTk5OUtLSw4ODqQwMTHBA3J+/vzp7e29tb"
//            + "WFUEgKUOax68Ax+vv7ExMTzczMSPOPkZyczJvIQR6Ji4tDQ3DElZUVGiIaGhpqa2vRcHJyIoWzszMa0gYUampqhAXvAgJwSkpKbm4ubhS2O"
//            + "zAwwGvLKSwsRM47PT1lbYlE+XfA7/7s2TNcNmxXX18fo7ABHruN8fHxu4QXXOr8/DyvoYm9vT1KJfBLmmJkZERDBGwdn4QG7J4UPD09aUgd"
//            + "fNjc3BzOCtAA4eHhNEsnYFUqN6oRhGRsgAZORhNhAzQEvnz5kp2dTW2okQLOQRIVLi8vra2tSUcjcGVuaQS3RddzM7jdsLAwag8ODtLcgIA"
//            + "AksAk4HknJyfU7ezsJIXXr1+TRB24IOmo8+TJk4ODA+5oBEUZL3Mjr169+vr1K7WFYujly5ckgSd1d3dTG1RWVpJCfn4+i9TAd7579079vv"
//            + "39/b99+ybciwZgc0JyvoGRkZHY2FjuyGTV1dU0HUUFuqurq5SrBYRiA8dikRZg2aOjo1gQ2aCxsXF2dpbkUVFRtIIGEKFI6QawrpeXl3JoK"
//            + "ygooOmlpaX4ZpiNSsrEL0AKTU1NLPodPn/+/OjRI1pBA4uLi6yonffv3wseRiAm0nSEZFRCUOABBfiJSaGjo4NFOoPEZGdnR9M1Ay9mXQXb"
//            + "29tdXV3ckfufvb09niTclxMZGUnTccfwRVgnDyiA1ZECHRoGQPJbmZmZEXKtVlhXASwBzyd8K/dlspCQkPb2du4oEAoPQ0NDjaHd0dGRFOD"
//            + "l8AdtNbcAbAxe/ubNG0pJt6ByhfCG+vp67shk+PVxPnVPdXZ2pulwHRZdx9zcnBSQnHFzyrvs7u4i+JSXlyPpvn37Ft8TFBT0+PFj0tcJXA"
//            + "MvJpNNTU0hAAlvE3SRdBYWFqirjIWFBeZGR0dzXw2hkMBPgXVYKgclAKI4qnAcuri4GD8FnhekrCsZGRm8mEzm6+srxLWdnR2YckVFBXVVw"
//            + "FGQCxH/ua+Gra0tra/sHhqh4EPKuoIrIQv+9OkTujBotOENLi4uqOJh4vKVVUH03dzc5I4m4uPjkWjV3wcqYH3hKfl7oDzA50qlUupSdYsy"
//            + "enl5mddWsLGxcXFxwZ0bgRoqPu5cB6EG64yNjSFrurq60qZ3AdWZqakpd+TA7HgTBUh7MBgU+NqKem3ASfBQgE+j2MDL/F6et0hF6skCLyg"
//            + "rKytSwK4o9xDOeUwLiBiIlajI/1N1ryPIdhShYHBI3c3NzcITVRk9PT04AEbxxj48PKSwCJNA8dDS0oLs86D/FhMRERERERERERERedhIJL"
//            + "8AdyCUxQa+NNgAAAAASUVORK5CYII=";
//
//    @Before
//    public void before() throws Exception {
//        model = new SimpleBase64Model(keyValue);
//        model.setDesc(descValue);
//        model.setImageTitle(entryTitleValue);
//        assertThat("Test image missing.", getClass().getResource(imagePath), is(notNullValue()));
//        entryImageValue = getImageFromPath(imagePath);
//        model.setImageData(entryImageValue);
//    }
//
//    @After
//    public void after() {
//    }
//
//    @Test
//    public void annotationParsingSimpleTest() {
//        MasterdataMapperCached simpleMapper = new MasterdataMapperCached(SimpleBase64Model.class);
//        assertThat(simpleMapper, is(notNullValue()));
//        assertThat(simpleMapper.getModel(), is(notNullValue()));
//
//        assertThat(simpleMapper.getPool(), is(notNullValue()));
//        assertThat(simpleMapper.getPool(), is(pool));
//
//        assertThat(simpleMapper.getKeyField(), is(notNullValue()));
//        assertThat(simpleMapper.getKeyField().getName(), is(keyName));
//
//        assertThat(simpleMapper.getGroupField(), is(notNullValue()));
//        assertThat(simpleMapper.getGroupField().getName(), is(groupName));
//
//        assertThat(simpleMapper.getDescField(), is(notNullValue()));
//        assertThat(simpleMapper.getDescField().getName(), is(descName));
//
//        assertThat(simpleMapper.getFilter1Field(), is(notNullValue()));
//        assertThat(simpleMapper.getFilter1Field().getName(), is(entryTitleFieldName));
//
//        assertThat(simpleMapper.getEntryFieldMap().size(), is(2));
//        assertThat(simpleMapper.getEntryFieldMap(), hasKey(entryTitleName));
//        assertThat(simpleMapper.getEntryFieldMap().get(entryTitleName).getName(), is(entryTitleName));
//        assertThat(simpleMapper.getEntryFieldMap(), hasKey(entryImageName));
//        assertThat(simpleMapper.getEntryFieldMap().get(entryImageName).getName(), is(entryImageName));
//    }
//
//    @Test
//    public void fieldGetHandleSimpleTest() {
//        MasterdataMapperCached simpleMapper = new MasterdataMapperCached(SimpleBase64Model.class);
//        assertThat(simpleMapper, is(notNullValue()));
//
//        assertThat(simpleMapper.getPool(model), is(pool));
//        assertThat(simpleMapper.getKey(model), is(keyValue));
//        assertThat(simpleMapper.getGroup(model), is(groupValue));
//        assertThat(simpleMapper.getDescription(model), is(descValue));
//        assertThat(simpleMapper.getFilter1(model), is(entryTitleValue));
//    }
//
//    @Test
//    public void fieldGetHandleEntriesSimpleTest() {
//        MasterdataMapperCached simpleMapper = new MasterdataMapperCached(SimpleBase64Model.class);
//        assertThat(simpleMapper, is(notNullValue()));
//
//        List<MovilizerGenericDataContainerEntry> data = simpleMapper.getData(model);
//        assertThat(data.size(), is(2));
//
//        MovilizerGenericDataContainerEntry entryTitle = null;
//        MovilizerGenericDataContainerEntry entryImage = null;
//        for (MovilizerGenericDataContainerEntry entry : data) {
//            if (entryTitleName.equals(entry.getName())) {
//                entryTitle = entry;
//            }
//
//            if (entryImageName.equals(entry.getName())) {
//                entryImage = entry;
//            }
//        }
//        assertThat(entryTitle, is(notNullValue()));
//        assertThat(entryTitle.getValstr(), is(entryTitleValue));
//        assertThat(entryImage, is(notNullValue()));
//        assertThat(entryImage.getValb64(), is(entryImageValue));
//    }
//
//    // ----------------------------------------------------------------------------------------------------------- Utils
//    private byte[] getImageFromPath(String imagePath) throws IOException {
//        BufferedImage img = ImageIO.read(getClass().getResource(imagePath));
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//        ImageIO.write(img, "png", bos);
//        byte[] imgBytes = bos.toByteArray();
//        bos.close();
//        return imgBytes;
//    }
//
//    private String getBase64ImageFromPath(String imagePath) throws IOException {
//        BASE64Encoder encoder = new BASE64Encoder();
//        String imageString = encoder.encode(getImageFromPath(imagePath));
//        return imageString;
//    }
//
//    private BufferedImage getImageFromBase64(String base64Image) throws IOException {
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte[] imageByte = decoder.decodeBuffer(base64Image);
//        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//        BufferedImage image = ImageIO.read(bis);
//        bis.close();
//        return image;
//    }
//
//    private void saveImageToDisk(String path, BufferedImage image) throws IOException {
//        ImageIO.write(image, "png", new File(path));
//    }
}
