# Disketta 

### Key objects

* `DBExecutor`
* `CsvHelper`
* `XmlReader`
* `XmlWriter`

### Example of setting up `DBExecutor` with XML

```java
DBExecutor executor = new DBExecutor(new DBConfig("C:\\Settings\\Data.xml"));
```

##### Data.xml
```XML
<Data>
	<Settings>
		<!-- Desktop || Web -->
		<AppType>Desktop</AppType>
	</Settings>
	<ConnectionString>
		<ServerName>localhost</ServerName>
		<InstanceName>SQLEXPRESS</InstanceName>
		<DatabaseName>Testing</DatabaseName>
		<Username>Merlin</Username>
		<Password/>
		<Port>1433</Port>
		<IntegratedSecurity>true</IntegratedSecurity>
	</ConnectionString>
</Data>
```