package it.polimi.jasper.streams.items;


/**
 * Created by riccardo on 05/09/2017.
 */
public class StreamItemImpl<T> extends StreamItem<T> {
    public StreamItemImpl(long appTimestamp1, T content1, String stream_uri) {
        super(appTimestamp1, content1, stream_uri);
    }
}
