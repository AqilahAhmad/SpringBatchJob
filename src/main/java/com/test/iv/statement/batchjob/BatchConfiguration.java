package com.test.iv.statement.batchjob;

import com.test.iv.statement.model.AccountStatement;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job importJob(Step step) {
        return jobBuilderFactory.get("importJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end().build();
    }

    @Bean
    public Step step(JdbcBatchItemWriter writer) {
        return stepBuilderFactory.get("step")
                .<AccountStatement, AccountStatement> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader reader() {
        return new FlatFileItemReaderBuilder().name("dataItemReader")
                .resource(new ClassPathResource("/datasource/dataSource.txt"))
                .linesToSkip(1)
                .delimited()
                .delimiter("|")
                .names(new String[] {"accountNumber","trxAmount","description","trxDate","trxTime","customerId"})
                .fieldSetMapper(fieldSetMapper())
                .build();
    }

    @Bean
    public AccountStatementItemProcessor processor() {
        return new AccountStatementItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider())
                .sql("INSERT INTO account_statement (account_number, trx_amount, description, trx_date, trx_time, customer_id) " +
                        "VALUES (:accountNumber, :trxAmount, :description, :trxDate, :trxTime, :customerId)")
                .dataSource(dataSource)
                .build();
    }

    public ConversionService conversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(conversionService);
        conversionService.addConverter(new Converter<String, BigDecimal>() {
            @Override
            public BigDecimal convert(String text) {
                return BigDecimal.valueOf(Double.parseDouble(text)).setScale(2, RoundingMode.HALF_EVEN);
            }
        });
        conversionService.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String text) {
                return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });
        conversionService.addConverter(new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String text) {
                return LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss"));
            }
        });
        return conversionService;
    }

    public FieldSetMapper<AccountStatement> fieldSetMapper() {
        BeanWrapperFieldSetMapper<AccountStatement> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setConversionService(conversionService());
        mapper.setTargetType(AccountStatement.class);
        return mapper;
    }
}
