package annotations;


@AnnHtmlUL
public class DocumentClass {

    String author;


    @AnnHtmlUI(background = "blue",color = "black")
    public String getDocumentName()
    {
        return "Java Core";
    }

    @AnnHtmlUI(background = "yellow")
    public String getDocumentVersion()
    {
        return "1.0";
    }

    @AnnHtmlUI(background = "green")
    public void setAuthor(String author) {
        this.author = author;
    }

    @AnnHtmlUI(background = "red",color = "black")
    public String getAuthor() {
        return author;
    }

    public float getPrice()  {
        return 100;
    }


}
