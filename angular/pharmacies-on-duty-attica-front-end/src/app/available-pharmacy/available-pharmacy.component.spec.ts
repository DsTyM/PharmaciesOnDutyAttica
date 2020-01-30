import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AvailablePharmacyComponent} from './available-pharmacy.component';

describe('AvialblePharmacyComponent', () => {
  let component: AvailablePharmacyComponent;
  let fixture: ComponentFixture<AvailablePharmacyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AvailablePharmacyComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvailablePharmacyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
