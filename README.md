# MosMetro & JSON
Программа, выполняющая следующие действия:
1. Получает HTML-код страницы «Список станций Московского метрополитена» https://www.moscowmap.ru/metro.html#lines
2. Парсит полученную страницу и получает из неё:
   - линии московского метро (имя линии, номер линии).
   - станции московского метро (имя станции, номер линии).
3. Создаёт и записывает на диск JSON-файл со списком станций по линиям и списком линий.
4. Читает файл и выводит в консоль количество станций на каждой линии.
