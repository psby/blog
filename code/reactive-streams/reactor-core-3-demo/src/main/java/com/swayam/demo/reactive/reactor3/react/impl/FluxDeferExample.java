package com.swayam.demo.reactive.reactor3.react.impl;

import java.io.InputStream;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import com.swayam.demo.reactive.reactor3.model.LineItemRow;
import com.swayam.demo.reactive.reactor3.react.ReactiveXmlParser;
import com.swayam.demo.reactive.reactor3.xml.XmlEventListener;
import com.swayam.demo.reactive.reactor3.xml.XmlParser;

import reactor.core.publisher.Flux;

public class FluxDeferExample implements ReactiveXmlParser {

	@Override
	public Flux<LineItemRow> parse(InputStream inputStream) {

		Supplier<Publisher<LineItemRow>> supplier = () -> {
			Publisher<LineItemRow> lineItemRowPublisher = (Subscriber<? super LineItemRow> lineItemRowSubscriber) -> {
				try {
					XmlParser xmlParser = new XmlParser();
					xmlParser.parse(inputStream, LineItemRow.class, new XmlEventListener<LineItemRow>() {

						@Override
						public void element(LineItemRow element) {
							lineItemRowSubscriber.onNext(element);
						}

						@Override
						public void completed() {
							lineItemRowSubscriber.onComplete();
						}
					});
				} catch (XMLStreamException xmlStreamException) {
					lineItemRowSubscriber.onError(xmlStreamException);
				}

			};
			return lineItemRowPublisher;
		};
		Flux<LineItemRow> flux = Flux.defer(supplier);

		return flux;
	}

}