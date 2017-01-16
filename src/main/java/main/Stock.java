package main;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="query")
@XmlAccessorType(XmlAccessType.FIELD)
public class Stock {

    @XmlElementWrapper(name="results")
    @XmlElement(name="quote")
    private List<Quote> quotes;


    public List<Quote> getQuotes() {
        return quotes;
    }
}
