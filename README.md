# Ship_management
Разработать многопоточное приложение. Использовать возможности, предоставляемыепакетомjava.util.concurrent.Неиспользовать слово synchronized. Все сущности, желающие получить доступ к ресурсу, должны быть потоками.

Порт. Корабли заходят в порт для разгрузки/загрузки контейнеров. Число контейнеров, находящихся в текущий момент в порту и на корабле, должно быть неотрицательным и превышающим заданную грузоподъемность судна и вместимость порта. В порту работает несколько причалов. У одного причала может стоять один корабль. Корабль может загружаться у причала, разгружаться или выполнять оба действия.